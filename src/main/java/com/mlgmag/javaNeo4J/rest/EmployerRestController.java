package com.mlgmag.javaNeo4J.rest;

import com.mlgmag.javaNeo4J.dto.FriendPairDto;
import com.mlgmag.javaNeo4J.dto.FriendRequestDto;
import com.mlgmag.javaNeo4J.entity.employer.Employer;
import com.mlgmag.javaNeo4J.entity.employer.EmployerDataLayer;
import com.mlgmag.javaNeo4J.entity.employer.EmployerPropertiesLayer;
import com.mlgmag.javaNeo4J.service.EmployerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employer")
public class EmployerRestController {

    private final EmployerService employerService;

    public EmployerRestController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @PostMapping("/")
    public List<EmployerPropertiesLayer> createAllEmployers(@RequestBody List<Employer> employers) {
        return employerService.createEmployers(employers);
    }

    @DeleteMapping("/")
    public void deleteAllEmployers(@RequestBody List<Employer> employers) {
        employerService.deleteEmployers(employers);
    }

    @PostMapping("/friends")
    public void makeFriends(@RequestBody FriendRequestDto friendsRequest) {
        employerService.makeFriends(friendsRequest.getFriendPairs());
    }

    @DeleteMapping("/friends")
    public void unFriends(@RequestBody FriendRequestDto friendsRequest) {
        employerService.unFriend(friendsRequest.getFriendPairs());
    }

    @GetMapping("/{name}")
    public EmployerDataLayer getEmployerByName(@PathVariable("name") String employerName) {
        return employerService.getEmployerByName(employerName);
    }

    @GetMapping("/")
    public List<EmployerPropertiesLayer> getAllEmployers() {
        return employerService.getAllEmployers();
    }

    @PostMapping("/shorterDistance")
    public int computeShorterDistance(@RequestBody FriendPairDto friendPairDto) {
        return employerService.computeShortestDistance(friendPairDto);
    }

    @GetMapping("/shorterDistance")
    public Map<String, Map<String, Integer>> getAllDistances() {
        return employerService.getAllDistances();
    }
}
