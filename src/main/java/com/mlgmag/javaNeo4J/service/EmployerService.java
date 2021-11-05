package com.mlgmag.javaNeo4J.service;

import com.mlgmag.javaNeo4J.dto.FriendPairDto;
import com.mlgmag.javaNeo4J.entity.employer.Employer;
import com.mlgmag.javaNeo4J.entity.employer.EmployerDataLayer;
import com.mlgmag.javaNeo4J.entity.employer.EmployerPropertiesLayer;
import com.mlgmag.javaNeo4J.repository.EmployerRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployerService {

    private final EmployerRepository employerRepository;

    public EmployerService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    public List<EmployerPropertiesLayer> createEmployers(List<Employer> employers) {

        List<EmployerPropertiesLayer> createdEmployers = employers.stream()
                .filter(this::isNotExists)
                .map(employerRepository::createNewEmployer)
                .collect(Collectors.toList());

        List<EmployerPropertiesLayer> updatedEmployers = employers.stream()
                .filter(this::isExists)
                .map(employerRepository::updateEmployer)
                .collect(Collectors.toList());

        return Stream.concat(createdEmployers.stream(), updatedEmployers.stream()).collect(Collectors.toList());
    }

    public boolean isExists(Employer employer) {
        return employerRepository.isExists(employer);
    }

    public boolean isNotExists(Employer employer) {
        return !employerRepository.isExists(employer);
    }

    public void deleteEmployers(List<Employer> employers) {
        List<Employer> existingEmployers = employers.stream().filter(employerRepository::isExists).collect(Collectors.toList());
        existingEmployers.stream().filter(employerRepository::containsRelations).forEach(employerRepository::deleteAllRelations);
        existingEmployers.forEach(employerRepository::deleteEmployer);
    }

    public void makeFriends(List<FriendPairDto> friendPairs) {
        friendPairs.stream().filter(this::isNotFriends).forEach(this::makeFriends);
    }

    private void makeFriends(FriendPairDto friendPair) {

        if (friendPair.getFriend1().getName().equals(friendPair.getFriend2().getName())) {
            return;
        }

        employerRepository.makeFriends(friendPair.getFriend1(), friendPair.getFriend2());
        employerRepository.makeFriends(friendPair.getFriend2(), friendPair.getFriend1());
    }

    public boolean isFriends(FriendPairDto friendPair) {
        return employerRepository.isFriends(friendPair.getFriend1(), friendPair.getFriend2());
    }

    public boolean isNotFriends(FriendPairDto friendPair) {
        return !isFriends(friendPair);
    }

    public void unFriend(List<FriendPairDto> friendPairs) {
        friendPairs.stream().filter(this::isFriends).forEach(this::unFriend);
    }

    private void unFriend(FriendPairDto friendPair) {
        employerRepository.unFriend(friendPair.getFriend1(), friendPair.getFriend2());
        employerRepository.unFriend(friendPair.getFriend2(), friendPair.getFriend1());
    }

    public EmployerDataLayer getEmployerByName(String employerName) {
        return employerRepository.getEmployerByName(employerName);
    }

    public List<EmployerPropertiesLayer> getAllEmployers() {
        return employerRepository.getAllEmployers();
    }

    public int computeShortestDistance(FriendPairDto friendPairDto) {
        return employerRepository.computeShortestDistance(friendPairDto.getFriend1(), friendPairDto.getFriend2()).orElse(-1);
    }

    public Map<String, Map<String, Integer>> getAllDistances() {
        Map<String, Map<String, Integer>> result = new HashMap<>();

        List<EmployerPropertiesLayer> allEmployers = getAllEmployers();

        for (EmployerPropertiesLayer source : allEmployers) {
            Map<String, Integer> employerDistances = getEmployerDistances(source, allEmployers);
            result.put(source.getName(), employerDistances);
        }

        return result;
    }

    private Map<String, Integer> getEmployerDistances(EmployerPropertiesLayer employer, List<EmployerPropertiesLayer> allEmployers) {
        Map<String, Integer> result = new HashMap<>();
        Employer employer1 = new Employer();
        employer1.setName(employer.getName());

        Employer employer2 = new Employer();
        for (EmployerPropertiesLayer target : allEmployers) {
            employer2.setName(target.getName());
            if (employer1.getName().equals(employer2.getName())) {
                continue;
            }
            int distance = employerRepository.computeShortestDistance(employer1, employer2).orElse(-1);
            result.put(target.getName(), distance);
        }

        return result;
    }
}
