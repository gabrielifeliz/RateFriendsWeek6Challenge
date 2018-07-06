package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String displayHome(Model model) {
        model.addAttribute("friends", friendRepository.findAllByOrderByRatingDesc());
        return "index";
    }

    @GetMapping("/newfriend")
    public String addFriend(Model model) {
        model.addAttribute("friend", new Friend());
        return "newfriend";
    }

    @PostMapping("/process")
    public String processForm(@Valid @ModelAttribute("friend") Friend friend,
                              BindingResult result,
                              @RequestParam("file")MultipartFile file) {
        if (result.hasErrors() || file.isEmpty()) {
            return "newfriend";
        }

        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            friend.setImageCloudinary(uploadResult.get("url").toString());
            friendRepository.save(friend);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/newfriend";
        }
        return "redirect:/";
    }

    @RequestMapping("/like/{id}")
    public String likeCounter(@PathVariable("id") long id){
        Friend friend =  friendRepository.findById(id).get();
        if (friend.getRating() < 10) {
            friend.setRating(friend.getRating() + 1);
            friendRepository.save(friend);
        }
        return "redirect:/";
    }

    @RequestMapping("/dislike/{id}")
    public String dislikeCounter(@PathVariable("id") long id){
        Friend friend =  friendRepository.findById(id).get();
        if (friend.getRating() > 1) {
            friend.setRating(friend.getRating() - 1);
            friendRepository.save(friend);
        }
        return "redirect:/";
    }

    @RequestMapping("/update/{id}")
    public String updateFriend( @PathVariable("id") long id, Model model){
        model.addAttribute("friend", friendRepository.findById(id).get());
        return "newfriend";
    }

    @RequestMapping("/delete/{id}")
    public  String deleteFriend(@PathVariable("id") long id){
        friendRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/search")
    public String showSearchResults(HttpServletRequest request, Model model)
    {
        //Get the search string from the result form
        String searchString = request.getParameter("search");
        model.addAttribute("search", searchString);
        model.addAttribute("friends",
                friendRepository.findAllByNameContainingIgnoreCase(searchString));
        return "index";
    }

    /*@GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(
            @Valid @ModelAttribute("user") User user,
            BindingResult result, Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "login";
    }*/
}