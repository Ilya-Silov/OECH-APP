package com.example.oechapp.Service;

import com.example.oechapp.Entity.RequestDto.CreateUserRequest;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Entity.UserRoles;
import com.example.oechapp.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUserByRole(UserRoles role)
    {
        return userRepository.findByRole(role);
    }

    public User updateUser(Long id, User newUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(newUser.getFirstName());
            existingUser.setLastName(newUser.getLastName());
            existingUser.setEmail(newUser.getEmail());
            existingUser.setPassword(newUser.getPassword());
            existingUser.setBalance(newUser.getBalance());

            return userRepository.save(existingUser);
        } else {
            // Handle user not found
            return null;
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User uploadPhoto(Long userId, MultipartFile photo) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPhoto(fileStorageService.uploadPhoto(photo).orElse(user.getPhoto()));
        return userRepository.save(user);
    }





    //TODO: убрать метод
    @PostConstruct
    private void fillUsers()
    {
        logger.info("Заполнение пользователей...");
        if (!userRepository.findAll().isEmpty())
        {
            return;
        }

        User createUser = new User();
        createUser.setEmail("test1");
        createUser.setFirstName("test1");
        createUser.setLastName("test1");
        createUser.setPassword("test1");
        createUser(createUser);

        createUser = new User();
        createUser.setEmail("test2");
        createUser.setFirstName("test2");
        createUser.setLastName("test2");
        createUser.setPassword("test2");
        createUser(createUser);



// Создание доставщика 1
        User deliver1 = new User();
        deliver1.setEmail("deliver1");
        deliver1.setFirstName("Sophia");
        deliver1.setLastName("Johnson");
        deliver1.setPassword("deliver1");
        deliver1.setRole(UserRoles.DELIVER);
        deliver1.setPhoto(tryToLoadAvatar("https://s3-alpha-sig.figma.com/img/2dfd/183f/ad3f15d2c9ff40423c93ff5da63a8cdb?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=nni~uWL6ND6YL5UaajjmLM-GMeaPGMYs3oq8pPaRlGZ6oUpW7nSj57XFTFDltrmfqaRF6vdZ1F22PYNzdr0neHXC4zdZnU1J5ey8f6L5ERXzjBGV2KuCg0hXVjUm2~K2Cxb7PDXrDoSa2U2auygm7Wq5DLdNjwERZZdRErbuq7aY9SXTUvRAa5D-XkqMcRiZJLwd8ziztHazLUYR5KiunL8Nwu-WN-cxmc7c3jC4d8ZiUyY2pIhusBahLKDiZCYRTuDMtb0oew~o0MyGblT6fa13AT7alLP87yZ~SSIlnOkP6l2eV5K0-kqKRE1iSLCHHOqupWTO-9dRoSuVY8g58w__"));

        createUser(deliver1);

// Создание доставщика 2
        User deliver2 = new User();
        deliver2.setEmail("deliver2");
        deliver2.setFirstName("Liam");
        deliver2.setLastName("Garcia");
        deliver2.setPassword("deliver2");
        deliver2.setRole(UserRoles.DELIVER);
        deliver2.setPhoto(tryToLoadAvatar("https://s3-alpha-sig.figma.com/img/18ce/c8ef/eca07f14d90b1cea20ad14f7065591f4?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=pWpLcVuQfVwe4viGI0rbMimeIQm7pFtQ8FUaOVNL~Fm5w-7HerghyQZJz4gAEDS55PPwlgo67AKKL72304eLsyxdVNyuatBqR50gCDVSdpEBq1igK1S9390dbhboE5LLm9tUAybo-G3UzQf8arrsOncc9RRBLBn196b6ff83ZlsxrG8~XHXKT50W3~uMAcb5J9rGGwITdoVBVCXY74iGx9ElEYnt2Cek8~t9Kv6GaFb9SlPJl-vxSa9~Soh1mMID6xAmgXvjy8b3XjTHW4d218te~0-qmTIHJWNZCsMgpqZNcXbyU7oDEJRBHPjENPV~rjJGP9mUtYfkJScyHVX44g__"));
        createUser(deliver2);

// Создание доставщика 3
        User deliver3 = new User();
        deliver3.setEmail("deliver3");
        deliver3.setFirstName("Isabella");
        deliver3.setLastName("Smith");
        deliver3.setPassword("deliver3");
        deliver3.setRole(UserRoles.DELIVER);
        deliver3.setPhoto(tryToLoadAvatar("https://s3-alpha-sig.figma.com/img/59da/1f67/4ba0f66d30a0cbd38f18b76aefba3455?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=niXrl3CTy5KYBTZdF1PP0FDKTeX5ruyruHl6uG50~VLPvQ621AhL4rbMmWv52r-nJQpyw3tt6xdfunHxen9egm31P8a5kPGCEHJAMWr3ZV-xLVaVXRv5ymf1SMX06pQB48vI28sNCjlVA9Fg8PUo3VE22FEph1ZCJLKdfpvVCtF6-mcA4FYBmoIOEvcIgxJa8E9a~8hZmmyehDppELdjQQO~wU8IySHx3XAYy4ASKmHxdYg0X0gizAAh~fKWRGJYtcskhwlZPWZKjUpN08xY7lPGdpvTcLTQPITY5UQBIceMpoM61YRhlvVW~b2WPDqFMkMQSDiL3wXQxyBUA~OQvQ__"));
        createUser(deliver3);

// Создание доставщика 4
        User deliver4 = new User();
        deliver4.setEmail("deliver4");
        deliver4.setFirstName("Noah");
        deliver4.setLastName("Brown");
        deliver4.setPassword("deliver4");
        deliver4.setRole(UserRoles.DELIVER);
        deliver4.setPhoto(tryToLoadAvatar("https://s3-alpha-sig.figma.com/img/615c/e206/094a9d23733dc1e7db99a2e928a55510?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=mlQR~acUi1CPIKGaNhDQge8cZViYmiGqNWqCbZisq9~G51tefUQ7D6R7h3ZM8uSX6O7oUW4dBe8qLoi~JyZgYC1ZFf33MHvsewnPz28tRg9jd60F8WnC6hLUNMwyfxUJRzkEDsNxwn1Q3v19DWSN-2M1RvrJ69TJNB8CL4QxjPdgAbM0cKdENgAiZObHQoNJi3b7ilaxO5mgJ-fTMfh2KiixRIZWCM3b23Ws-nCMvRmq6aRaf-KZkZA3WraIzwM3xkq0Tx1OJA0W-eVHRR8U1kso~cWBXU4FXNzq-BnEVIBAjQfpHgtgu3XF57Et96PjMTwN31J6Pl5mHnYAHX2zsg__"));
        createUser(deliver4);

// Создание доставщика 5
        User deliver5 = new User();
        deliver5.setEmail("deliver5");
        deliver5.setFirstName("Olivia");
        deliver5.setLastName("Martinez");
        deliver5.setPassword("deliver5");
        deliver5.setRole(UserRoles.DELIVER);
        deliver5.setPhoto(tryToLoadAvatar("https://s3-alpha-sig.figma.com/img/52cc/7536/9cd8ed6ea9f81e5687aabc51498950cf?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=bVVxcPHfhuyp3bZ~OV8lz9iV5QLDlaOCUqOfeTCbsikvcqPTYJILkGEzrmFzp-Poa75UbnR0-3pYMfS3Ec6d511VkB6ccD6hDiELyry90CJp8gDhnk6bMys322Rv670SPpAJ7gQAXKSgT4oh-GF49zTyNY3OaMe9qxOj3U8tmKEPaXFBTfmG3dPIzlEVtzsZQMfr7ytQBhwzgYfX~g8eFE559wZ0FSuJk5exFP5ElHrSJMOw3nLaW8-ctqv~LoHi3i9FVHFdMh9K5htYfP3RovI2WGsuSXwYQ2MgkIC9ABwYgaYViy-nIhNDQWwZ5t8FcfMCHS7sdOEzmHhGz~WAdQ__"));
        createUser(deliver5);
    }

    private String tryToLoadAvatar(String url)
    {
        try {
            Optional<String> photopath = fileStorageService.uploadPhotoFromURL(url);
            return photopath.orElse(null);
        } catch (IOException io) {
            logger.error("Не удалось загрузить фото - " + io.getMessage());
            return null;
        }
    }
}


