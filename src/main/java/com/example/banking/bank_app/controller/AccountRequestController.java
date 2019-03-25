package com.example.banking.bank_app.controller;

import com.example.banking.bank_app.model.Account;
import com.example.banking.bank_app.model.AccountRequest;
import com.example.banking.bank_app.model.Config;
import com.example.banking.bank_app.model.User;
import com.example.banking.bank_app.service.AccountRequestService;
import com.example.banking.bank_app.service.AccountService;
import com.example.banking.bank_app.service.EmployeeService;
import com.example.banking.bank_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value="/account-request")
public class AccountRequestController {
    @Autowired
    AccountService accountService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    UserService userService;

    @Autowired
    AccountRequestService accountRequestService;

    @RequestMapping(value="/list/{page}", method= RequestMethod.GET)
    public ModelAndView list(@PathVariable("page") int page, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<String>();
        for(GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }
        int role;
        ModelAndView modelAndView;
        if(roles.contains("ADMIN")){
            role = Config.ADMIN;
            modelAndView = new ModelAndView("account_request_admin");
        }else{
            role = Config.TIER1;
            modelAndView = new ModelAndView("account_request");
        }

        PageRequest pageable = PageRequest.of(page - 1, 10);
        Page<AccountRequest> requestPage = accountRequestService.getPaginated(pageable, role);
        int totalPages = requestPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNums = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNums", pageNums);
        }
        modelAndView.addObject("activeRequestList", true);
        modelAndView.addObject("requestList", requestPage.getContent());
        return modelAndView;
    }

    @RequestMapping(value="/approve/{id}", method= RequestMethod.POST)
    public ModelAndView approve(@PathVariable("id") int id, Authentication authentication)  throws IOException {
        Long userId =  employeeService.findUserByEmail(authentication.getName());
        String name = employeeService.getEmployeeById(userId).getEmployee_name();
        AccountRequest accountRequest = accountRequestService.getAccountByAccountNo(new Long(id));
        if(accountRequest.getType() == Config.ACCOUNT_TYPE){
            accountRequest.deserializeaccount();
            Map<String, Object> attributes = accountRequest.getAccountJson();
            Account account = new Account();
            account.setAccountNo((Long)attributes.get("account_no"));
            account.setUserId(new Long((Integer)attributes.get("user_id")));
            int balance = (int)attributes.get("balance");
            account.setBalance((float)balance);
            account.setRoutingNo((Integer)attributes.get("routing_no"));
            account.setAccountType((Integer)attributes.get("account_type"));
            double interest = (double)attributes.get("interest");
            account.setInterest((float)interest);
            account.setCreated(new Timestamp((Long) attributes.get("created")));
            account.setUpdated(new Timestamp((Long) attributes.get("updated")));
            accountService.saveOrUpdate(account);
        }else{
            accountRequest.deserializeuser();
            Map<String, Object> attributes = accountRequest.getUserJson();
            User user = new User();

        }
        accountRequest.setApproved_at(new Timestamp(System.currentTimeMillis()));
        accountRequest.setApproved_by(name); //Remeber to change this
        accountRequest.setStatus_id(Config.APPROVED);
        accountRequestService.saveOrUpdate(accountRequest);
        return new ModelAndView("redirect:/account-request/list/1");
    }

    @RequestMapping(value="/decline/{id}", method= RequestMethod.POST)
    public ModelAndView decline(@PathVariable("id") int id, Authentication authentication) {
        Long userId =  employeeService.findUserByEmail(authentication.getName());
        String name = employeeService.getEmployeeById(userId).getEmployee_name();
        AccountRequest accountRequest = accountRequestService.getAccountByAccountNo(new Long(id));
        accountRequest.setApproved_at(new Timestamp(System.currentTimeMillis()));
        accountRequest.setApproved_by(name); //Remeber to change this
        accountRequest.setStatus_id(Config.DECLINED);
        accountRequestService.saveOrUpdate(accountRequest);
        return new ModelAndView("redirect:/account-request/list/1");
    }

}
