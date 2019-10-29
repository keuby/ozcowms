package com.keuby.ozcowms.user;

import com.keuby.ozcowms.common.enums.PropertyType;
import com.keuby.ozcowms.user.domain.Property;
import com.keuby.ozcowms.user.domain.User;
import com.keuby.ozcowms.user.repository.PropertyRepository;
import com.keuby.ozcowms.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class UserApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyRepository propertyRepository;

    private final String openId = "abc123_";
    private final String name = "小星星";

    @Test
    @Transactional
    public void contextLoads() {
        User user = new User();
        user.setOpenId(openId);
        User userInDB = userService.create(user);
        Assert.assertNotNull(userInDB.getId());

        user = new User();
        user.setOpenId(openId);
        user.setName(name);
        userService.update(user);
        userInDB = userService.getByOpenId(openId);
        Assert.assertTrue(name.equals(userInDB.getName()));

        Property property = new Property();
        property.setName("生日");
        property.setDescription("birthday");
        property.setType(PropertyType.DATE);
        propertyRepository.save(property);
        Assert.assertTrue(property.getId() != null);
    }

}
