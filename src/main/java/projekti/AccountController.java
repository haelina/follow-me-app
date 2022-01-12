package projekti;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ImageObjectRepository imageObjectRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @GetMapping("/")
    public String showFront(Model model) {
        model.addAttribute("users", accountRepository.findAll());
        //model.addAttribute("message", "World!");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("currentUserName", auth.getName());
        }
        return "index";
    }
    
    @GetMapping("/users")
    public String showAccounts(@ModelAttribute Account account, Model model) {
        return "form";
    }
    
    @GetMapping("/users/search")
    public String searchFor(Model model, String keyword) {
        if(keyword == null || keyword.length() == 0) {
            model.addAttribute("users", new ArrayList<>());
        } else {
            model.addAttribute("users", 
                    accountRepository.findByProfileNameContaining(keyword));
        }
        model.addAttribute("keyword", "\"" + keyword + "\"");
        return "searchresults";
    }
    
    @PostMapping("/users")
    public String create(@Valid @ModelAttribute Account account,
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "form";
        }
        
        String pw = passwordEncoder.encode(account.getPassword());
        account.setImages(new ArrayList<>());
        account.setPassword(pw);
        account.setMessages(new ArrayList<>());
        account.setComments(new ArrayList<>());
        account.setLikedImages(new ArrayList<>());
        account.setLikedMessages(new ArrayList<>());
        account.setIFollow(new ArrayList<>());
        account.setMyFollowers(new ArrayList<>());
        
        accountRepository.save(account);
        return "redirect:/";
    }
    
    @GetMapping("/users/{id}")
    public String getMyPage(Model model, @PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("currentUserName", auth.getName());
        }
        Account a = accountRepository.findByProfileName(id);
        model.addAttribute("user", a);
        model.addAttribute("messages", messageRepository.findByAccount(a));
        return "mypage";
    }
    
    @GetMapping("/users/{id}/images")
    public String showGallery(Model model, @PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("currentUserName", auth.getName());
        }
        model.addAttribute("user", accountRepository.findByProfileName(id));
        model.addAttribute("images", accountRepository.findByProfileName(id).getImages());
        return "gallery";
    }
    
    @GetMapping("/users/{id}/followers")
    public String showFollowers(Model model, @PathVariable String id) {
        model.addAttribute("user", accountRepository.findByProfileName(id));
        model.addAttribute("followers", accountRepository.findByProfileName(id).getMyFollowers());
        model.addAttribute("toptext", "My Followers:");
        return "followers";
    }
    
    @GetMapping("/users/{id}/ifollow")
    public String showIFollow(Model model, @PathVariable String id) {
        model.addAttribute("user", accountRepository.findByProfileName(id));
        model.addAttribute("followers", accountRepository.findByProfileName(id).getIFollow());
        model.addAttribute("toptext", "I Follow:");
        return "followers";
    }
    
    @PostMapping("/users/{id}/images")
    public String save(@RequestParam("file") MultipartFile file, 
            @RequestParam String description, 
            @PathVariable String id) throws IOException {
        if(accountRepository.findByProfileName(id).getImages().size() < 10) {
            ImageObject fo = new ImageObject(file.getBytes(), description, 
                    accountRepository.findByProfileName(id), new ArrayList<>(),
                    new ArrayList<>());
            imageObjectRepository.save(fo);
        }

        return "redirect:/users/" + id + "/images";
    }
    
    @PostMapping("/users/{id}/profileimage/{imageId}")
    public String saveProfileImage(@PathVariable Long imageId, 
            @PathVariable String id) {
        Account a = accountRepository.findByProfileName(id);
        a.setProfileImageId(imageId);
        accountRepository.save(a);
        return "redirect:/users/" + id;
    }
    
    @GetMapping(path = "/images/{id}/content", produces = "image/jpg")
    @ResponseBody
    public byte[] getPic(@PathVariable Long id) {
        System.out.println("Id is: " + id);
        return imageObjectRepository.getOne(id).getContent();
    }
    
    @PostMapping("/users/{id}/images/{imageId}")
    public String deleteImage(@PathVariable Long imageId, 
            @PathVariable String id) {
        if(accountRepository.findByProfileImageId(imageId) != null) {
            Account a = accountRepository.findByProfileImageId(imageId);
            a.setProfileImageId(null);
            imageObjectRepository.deleteById(imageId);
        } else if (imageObjectRepository.findById(imageId)!= null) {
            imageObjectRepository.deleteById(imageId);
        }
        return "redirect:/users/" + id + "/images";
    }
    
    @PostMapping("/users/{id}/messages")
    public String writeMessage(@RequestParam String message, 
            @PathVariable String id) {
        Account a = accountRepository.findByProfileName(id);
        Message m = new Message(a, message, LocalDateTime.now(),
        new ArrayList<>(), new ArrayList<>());
        messageRepository.save(m);
        return "redirect:/users/" + id;
    }
    
    @PostMapping("/users/{profileName}/images/{id}/comments")
    public String commentPic(@RequestParam String comment, 
            @PathVariable String profileName, 
            @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            ImageObject i = imageObjectRepository.getOne(id);
            String currentUserName = auth.getName();
            Account a = accountRepository.findByUserName(currentUserName);
            Comment c = new Comment(i, null, comment, a, LocalDateTime.now());
            commentRepository.save(c);
        }
        return "redirect:/users/" + profileName + "/images";
    }
    
    @PostMapping("/users/{profileName}/messages/{id}/comments")
    public String commentMessage(@RequestParam String comment, 
            @PathVariable String profileName, 
            @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Message m = messageRepository.getOne(id);
            String currentUserName = auth.getName();
            Account a = accountRepository.findByUserName(currentUserName);
            Comment c = new Comment(null, m, comment, a, LocalDateTime.now());
            commentRepository.save(c);
        }
        
        return "redirect:/users/" + profileName;
    }
    
    
    @PostMapping("/users/{id}/images/{imageId}/like")
    public String likeImage(@PathVariable String id, 
            @PathVariable Long imageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            ImageObject i = imageObjectRepository.getOne(imageId);
            String currentUserName = auth.getName();
            Account a = accountRepository.findByUserName(currentUserName);
            List<Account> likes = i.getLikedBy();
            if(likes.contains(a)) {
                likes.remove(a);
            } else {
                likes.add(a);
            }
            i.setLikedBy(likes);
            imageObjectRepository.save(i);
        }
        
        return "redirect:/users/" + id + "/images";
    }
    
    @PostMapping("/users/{id}/messages/{messageId}/like")
    public String likeMessage(@PathVariable String id, 
            @PathVariable Long messageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Message m = messageRepository.getOne(messageId);
            String currentUserName = auth.getName();
            Account a = accountRepository.findByUserName(currentUserName);
            List<Account> likes = m.getLikedBy();
            if(likes.contains(a)) {
                likes.remove(a);
            } else {
                likes.add(a);
            }
            m.setLikedBy(likes);
            messageRepository.save(m);
        }
        
        return "redirect:/users/" + id;
    }
    
    @PostMapping("/users/{id}/follow")
    public String startFollowing(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Account followThis = accountRepository.findByProfileName(id);
            String currentUserName = auth.getName();
            Account follower = accountRepository.findByUserName(currentUserName);
            List<Account> followers = followThis.getMyFollowers();
            if(followers.contains(follower)) {
                followers.remove(follower);
            } else {
                followers.add(follower);
            }
            followThis.setMyFollowers(followers);
            accountRepository.save(followThis);
        }
        
        return "redirect:/";
    }
}
