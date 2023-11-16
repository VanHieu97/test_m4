package com.example.controller;

import com.example.model.Customer;
import com.example.model.CustomerForm;
import com.example.service.CustomerService;
import com.example.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;
    @Value("${file_upload}")
    private String fileUpload;

    @GetMapping("")
    public String index(Model model) {
        List<Customer> customerList = customerService.getAll();
        model.addAttribute("customers", customerList);
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("customer", new CustomerForm());
        return "/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CustomerForm customerForm) {
        MultipartFile file = customerForm.getImage();
        String nameImage = file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(fileUpload + nameImage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Customer customer = new Customer(customerForm.getId(), customerForm.getName(), customerForm.getEmail(), customerForm.getAddress(), nameImage);
        customerService.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CustomerForm customerForm) {
        Customer customer = customerService.findById(customerForm.getId());
        Customer customer1 = null;
        MultipartFile file = customerForm.getImage();
        if (!file.isEmpty()) {
            String nameImage = file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + nameImage));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            customer1 = new Customer(customerForm.getId(), customerForm.getName(), customerForm.getEmail(), customerForm.getAddress(), nameImage);
        } else {
            customer1 = new Customer(customerForm.getId(), customerForm.getName(), customerForm.getEmail(), customerForm.getAddress(), customer.getImage());
        }
        customerService.save(customer1);
        return "redirect:/customers";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "/delete";
    }

    @PostMapping("/delete")
    public String delete(Customer customer, RedirectAttributes redirectAttributes) {
        customerService.delete(customer.getId());
        redirectAttributes.addFlashAttribute("success", "remove customer successfully");
        return "redirect:/customers";
    }

    @GetMapping("/{id}/view")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "view";
    }

    @GetMapping("/search")
    public ModelAndView showSearch(@RequestParam(name = "q") String q) {
        List<Customer> customers;
        if (q.isEmpty()) customers = customerService.getAll();
        else customers = customerService.findCustomerByName(q);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }
}

