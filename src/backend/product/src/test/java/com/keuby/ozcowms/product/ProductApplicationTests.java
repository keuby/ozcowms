package com.keuby.ozcowms.product;

import com.keuby.ozcowms.product.domain.Product;
import com.keuby.ozcowms.product.domain.Unit;
import com.keuby.ozcowms.product.domain.Storage;
import com.keuby.ozcowms.product.domain.UserStorage;
import com.keuby.ozcowms.product.model.AmountOperation;
import com.keuby.ozcowms.product.respository.ProductRepository;
import com.keuby.ozcowms.product.respository.StorageRepository;
import com.keuby.ozcowms.product.respository.UserStorageRepository;
import com.keuby.ozcowms.product.service.ProductService;
import com.keuby.ozcowms.product.service.UnitService;
import com.keuby.ozcowms.product.service.StorageService;
import com.keuby.ozcowms.product.service.UserStorageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ProductApplication.class })
public class ProductApplicationTests {

    private Long userId = 12345L;
    private Long operationUserId = 12345L;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private UserStorageRepository userStorageRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private UserStorageService userStorageService;

    @Test
    @Transactional
    public void contextLoads() {
        // 测试 Product Service
        Product product1 = new Product();
        product1.setName("全脂奶粉");
        product1.setCategory("milk-powder");
        productRepository.save(product1);
        Assert.assertNotNull(product1.getId());

        Product product2 = new Product();
        product2.setName("脱脂奶粉");
        product2.setCategory("milk-powder");
        productRepository.save(product2);
        Assert.assertNotNull(productService.getById(product2.getId()));

        // 测试 Unit Service
        Unit unit = unitService.create("袋", "milk-unit");
        Assert.assertNotNull(unit.getId());

        Unit cUnit = unitService.create("箱", 6, unit);
        Assert.assertNotNull(cUnit.getId());

        Unit ccUnit = unitService.create("件", 6, cUnit.getId());
        unitService.updateMultiple(ccUnit.getId(), 10);

        Unit topUnit = unitService.findTopUnitByTag("milk-unit");
        Assert.assertNotNull(topUnit);
        Assert.assertEquals("袋", topUnit.getName());

        unitService.delete(ccUnit.getId());
        Assert.assertNull(cUnit.getChild());

        // 测试 Storage Service 和 Transaction Service
        Storage storage1 = new Storage();
        storage1.setProduct(product1);
        storage1.setUnit(unit);
        storageRepository.save(storage1);
        UserStorage userStorage1 = new UserStorage();
        userStorage1.setUserId(userId);
        userStorage1.setStorage(storage1);
        userStorageRepository.save(userStorage1);

        Storage storage2 = new Storage();
        storage2.setProduct(product2);
        storage2.setUnit(unit);
        storageRepository.save(storage2);
        UserStorage userStorage2 = new UserStorage();
        userStorage2.setUserId(userId);
        userStorage2.setStorage(storage2);
        userStorageRepository.save(userStorage2);


        List<UserStorage> userStorageList = userStorageService.findByUserId(userId);
        Assert.assertEquals(2, userStorageList.size());

        storageService.addTotal(userId, new AmountOperation(userId, null, null, product1.getId(), Arrays.asList(
                new AmountOperation.Amount(unit.getId(), 1L),
                new AmountOperation.Amount(cUnit.getId(), 3L)
        ))); // 19
        storageService.addStaged(userId, new AmountOperation(userId, null, null, product1.getId(), Arrays.asList(
                new AmountOperation.Amount(unit.getId(), 5L),
                new AmountOperation.Amount(cUnit.getId(), 2L)
        ))); // 17
        storageService.sale(userId, new AmountOperation(userId, null, null, product1.getId(), Arrays.asList(
                new AmountOperation.Amount(unit.getId(), 2L),
                new AmountOperation.Amount(cUnit.getId(), 1L)
        ))); // 8

        Assert.assertEquals(19L, (long) storage1.getTotal());
        Assert.assertEquals(17L, (long) storage1.getStaged());
        Assert.assertEquals(8L, (long) storage1.getSales());

        Assert.assertEquals(2, productService.findProductsByUserId(userId).size());
        Assert.assertEquals(2, userStorageService.findByUserIdAndCategory(userId, "milk-powder").size());
    }

}
