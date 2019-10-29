package com.keuby.ozcowms.product.service.impl;

import com.keuby.ozcowms.common.exception.ServiceException;
import com.keuby.ozcowms.product.domain.Product;
import com.keuby.ozcowms.product.domain.Storage;
import com.keuby.ozcowms.product.domain.Unit;
import com.keuby.ozcowms.product.domain.UserStorage;
import com.keuby.ozcowms.product.respository.ProductRepository;
import com.keuby.ozcowms.product.respository.StorageRepository;
import com.keuby.ozcowms.product.respository.UnitRepository;
import com.keuby.ozcowms.product.respository.UserStorageRepository;
import com.keuby.ozcowms.product.service.UserStorageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserStorageServiceImpl implements UserStorageService {

    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final UserStorageRepository userStorageRepository;

    public UserStorageServiceImpl(
            UnitRepository unitRepository,
            ProductRepository productRepository,
            StorageRepository storageRepository,
        UserStorageRepository userStorageRepository) {
        this.unitRepository = unitRepository;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
        this.userStorageRepository = userStorageRepository;
    }

    @Override
    public UserStorage findByUserIdAndProduct(Long userId, Long productId) {
        Optional<UserStorage> optional = userStorageRepository.findByUserIdAndProductId(userId, productId);
        return optional.orElse(null);
    }

    @Override
    public List<UserStorage> findByUserIdAndCategory(Long userId, String category) {
        return userStorageRepository.findByUserIdAndCategory(userId, category);
    }

    @Override
    public List<UserStorage> findByUserId(Long userId) {
        return userStorageRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public List<UserStorage> init(long userId, String category, String unitName) {
        Optional<Unit> unit = unitRepository.findByNameAndTag(unitName, UnitRepository.COMMON_UNIT_TAG);
        if (!unit.isPresent()) {
            throw new ServiceException("单位:" + unitName + " 不存在");
        }
        List<Product> products = productRepository.findByCategory(category);
        if (products == null || products.size() <= 0) {
            throw new ServiceException("产品种类:" + category + " 不存在");
        }
        List<Storage> storages = storageRepository.saveAll(products.stream().map(product -> {
            Storage storage = new Storage();
            storage.setUnit(unit.get());
            storage.setProduct(product);
            return storage;
        }).collect(Collectors.toList()));
        return userStorageRepository.saveAll(storages.stream().map(storage -> {
          UserStorage userStorage = new UserStorage();
            userStorage.setStorage(storage);
            userStorage.setUserId(userId);
          return userStorage;
        }).collect(Collectors.toList()));
    }
}
