package com.keuby.ozcowms.product.service.impl;

import com.keuby.ozcowms.common.exception.ServiceException;
import com.keuby.ozcowms.product.domain.Unit;
import com.keuby.ozcowms.product.respository.UnitRepository;
import com.keuby.ozcowms.product.service.UnitService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

    public UnitServiceImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }


    @Override
    public Unit create(String name, int multiple, Long parentUnitId) {
        Unit parentUnit = null;
        if (parentUnitId != null) {
            Optional<Unit> parentUnitOptional = unitRepository.findById(parentUnitId);
            if (!parentUnitOptional.isPresent()) {
                throw new ServiceException("父级单位不存在");
            }
            parentUnit = parentUnitOptional.get();
        }
        return create(name, multiple, parentUnit);
    }

    @Override
    public Unit findTopUnitByTag(String tag) {
        return unitRepository.findByTag(tag).orElse(null);
    }

    @Override
    public Unit create(String name, String tag) {
        if (StringUtils.isEmpty(name)) {
            throw new ServiceException("单位名称不能为空");
        }
        Unit unit = new Unit();
        unit.setName(name);
        unit.setMultiple(1);
        unit.setTag(tag);
        return unitRepository.save(unit);
    }

    @Override
    public Unit create(String name, int multiple, Unit parentUnit) {
        if (StringUtils.isEmpty(name)) {
            throw new ServiceException("单位名称不能为空");
        }
        Unit unit = new Unit();
        unit.setParent(parentUnit);
        unit.setName(name);
        unit.setMultiple(multiple);
        return unitRepository.save(unit);
    }

    @Override
    public void updateMultiple(long unitId, int multiple) throws ServiceException {
        Optional<Unit> unitOptional = unitRepository.findById(unitId);
        if (!unitOptional.isPresent()) {
            throw new ServiceException("被修改换算倍数的单位不存在");
        }
        Unit unit = unitOptional.get();
        unit.setMultiple(multiple);
        unitRepository.save(unit);
    }

    @Override
    public void delete(long unitId) throws ServiceException {
        Optional<Unit> unitOptional = unitRepository.findById(unitId);
        if (!unitOptional.isPresent()) {
            return;
        }
        Unit unit = unitOptional.get();
        Unit child = unit.getChild();
        if (child != null) {
            throw new ServiceException("被删除的单位还存在子单位", child);
        }
        unitRepository.delete(unit);
    }

    @Override
    public Unit getById(long id) {
        return unitRepository.findById(id).orElse(null);
    }

    @Override
    public int computeMultiple(Unit src, Unit dst, boolean isChild) {
        if (src == null || dst == null) {
            return 0;
        }
        return isChild ? computeChildMultiple(src, dst) : computeParentMultiple(src, dst);
    }

    private int computeChildMultiple(Unit src, Unit dst) {
        if (src == null || dst == null) {
            return 0;
        }
        int multiple = src.getMultiple();
        if (src.getId().equals(dst.getId())) {
            return 1;
        } else if (src.getChild().getId().equals(dst.getId())) {
            return multiple;
        } else {
            return multiple * computeChildMultiple(src.getChild(), dst);
        }
    }

    private int computeParentMultiple(Unit src, Unit dst) {
        if (src == null || dst == null) {
            return 0;
        }
        int multiple = src.getMultiple();
        if (src.getId().equals(dst.getId())) {
            return 1;
        } else if (src.getParent().getId().equals(dst.getId())) {
            return multiple;
        } else {
            return multiple * computeParentMultiple(src.getParent(), dst);
        }
    }
}
