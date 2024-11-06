package com.example.userservice.controller;

import com.example.userservice.dto.Result;
import com.example.userservice.dto.StatusCode;
import com.example.userservice.dto.UserRq;
import com.example.userservice.dto.UserRs;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/user")
public class UserController {

    private final UserService userService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    Result createUser(@Valid @RequestBody UserRq rq){
        UserRs rs = userService.createUser(rq);
        return new Result(true, StatusCode.SUCCESS, "User created successfully", rs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result findById(@PathVariable Long id) {

        UserRs rs = userService.findById(id);

        return new Result(true, StatusCode.SUCCESS, "Found one success", rs);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result findAll() {

        List<UserRs> usersRs = userService.findAll();

        return new Result(true,StatusCode.SUCCESS, "Found all", usersRs);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result update(@PathVariable Long id, @RequestBody @Valid UserRq rq) {

        UserRs rs = userService.update(id, rq);

        return new Result(true,StatusCode.SUCCESS,"Update success", rs);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result deleteById(@PathVariable Long id) {

        userService.deleteById(id);

        return new Result(true,StatusCode.SUCCESS, "Delete success");
    }

    @PostMapping("/change-role/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result assignRoleToUser(@PathVariable String username, @RequestBody String roleName){

        userService.assignRoleToUser(username, roleName);

        return new Result(true,StatusCode.SUCCESS,"Role assign success");
    }

}
