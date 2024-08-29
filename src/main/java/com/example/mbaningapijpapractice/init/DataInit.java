package com.example.mbaningapijpapractice.init;


import com.example.mbaningapijpapractice.auth.RoleRepository;
import com.example.mbaningapijpapractice.domain.AccountType;
import com.example.mbaningapijpapractice.domain.CardType;
import com.example.mbaningapijpapractice.domain.Role;
import com.example.mbaningapijpapractice.domain.User;
import com.example.mbaningapijpapractice.features.account.AccountTypeRepository;
import com.example.mbaningapijpapractice.features.card.CardTypeRepository;
import com.example.mbaningapijpapractice.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final CardTypeRepository cardTypeRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        initCard();
        AccountTypeInit();
        roleInit();
        UserInit();
    }

    private void initCard() {
        CardType visa = new CardType();
        visa.setName("Visa");
        visa.setAlias("visa");
        visa.setDeleted(false);

        CardType masterCard = new CardType();
        masterCard.setName("MasterCard");
        masterCard.setAlias("master");
        masterCard.setDeleted(false);
        cardTypeRepository.saveAll(List.of(visa, masterCard));

    }

    private void AccountTypeInit() {
        AccountType saving = new AccountType();
        saving.setName("Savings");
        saving.setAlias("Savings");
        saving.setDescription("Sasdsdadasdasdsa");
        saving.setIsDeleted(false);

        AccountType payroll = new AccountType();
        payroll.setName("Payroll");
        payroll.setAlias("payroll");
        payroll.setDescription("Sasdsdadasdasdsa");
        payroll.setIsDeleted(false);

        AccountType current = new AccountType();
        current.setName("Current");
        current.setAlias("current");
        current.setDescription("Skjkk32");
        current.setIsDeleted(true);
        accountTypeRepository.saveAll(List.of(saving, payroll, current));
    }

    private void UserInit() {
        List<Role> roles = new ArrayList<>();

        roles.add(roleRepository.findById(2).orElseThrow());
        User user1 = User.builder()
                .email("srunveng@cstad.com")
                .cityOrProvince("Phnom Penh City")
                .companyName("ISTAD Technology Center")
                .createdAt(LocalDate.now())
                .isBlock(false)
                .name("SrunVeng")
                .employeeType("Backend Engineer")
                .gender("male")
                .isVerified(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isDeleted(false)
                .mainSourceOfIncome("Salary")
                .khanOrDistrict("Don Penh")
                .monthlyIncomeRange(BigDecimal.valueOf(4500))
                .nationalCardId("32323")
                .phoneNumber("017627382")
                .oneSignalId("546")
                .pin("134883")
                .password(passwordEncoder.encode("Srun@123"))
                .uuid(UUID.randomUUID().toString())
                .roles(roles)
                .build();
        userRepository.save(user1);
    }

    private void roleInit() {
        List<Role> roles = new ArrayList<>();

        Role role1 = Role.builder()
                .name("ADMIN")
                .build();
        Role role2 = Role.builder()
                .name("MANAGER")
                .build();
        Role role3 = Role.builder()
                .name("STAFF")
                .build();
        Role role4 = Role.builder()
                .name("CUSTOMER")
                .build();
        roles.add(role1);
        roles.add(role2);
        roles.add(role3);
        roles.add(role4);
        roleRepository.saveAll(roles);
    }


}
