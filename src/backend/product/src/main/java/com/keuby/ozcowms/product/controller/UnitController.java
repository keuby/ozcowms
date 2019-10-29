package com.keuby.ozcowms.product.controller;

import com.keuby.ozcowms.common.response.JsonResp;
import com.keuby.ozcowms.product.domain.Unit;
import com.keuby.ozcowms.product.service.UnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/unit")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping(value = "/{tag}")
    public JsonResp findByTag(@PathVariable(value = "tag") String tag) {
        Unit unit = unitService.findTopUnitByTag(tag);
        if (unit == null) {
            return JsonResp.notFound(tag + " 不存在");
        }
        List<Unit> units = new ArrayList<>();
        do {
            units.add(unit);
            unit = unit.getChild();
        } while (unit != null);
        return JsonResp.ok(units);
    }
}
