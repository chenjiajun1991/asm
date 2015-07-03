package com.sam.yh.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.req.bean.CitySalesReq;
import com.sam.yh.resp.bean.CitySalesResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.BatteryService;

@RestController
@RequestMapping("/reseller")
public class ListCityBtysController {

    public static final Logger logger = LoggerFactory.getLogger(ListCityBtysController.class);

    @Autowired
    BatteryService batteryService;

    @RequestMapping(value = "/sales", method = RequestMethod.POST)
    public SamResponse submitBtySpec(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.debug("Request json String:" + jsonReq);

        CitySalesReq req = JSON.parseObject(jsonReq, CitySalesReq.class);
        List<Integer> cityIds = req.getCitys();
        List<Integer> citySales = new ArrayList<Integer>();

        for (Integer cityId : cityIds) {
            citySales.add(batteryService.countCityBtys(cityId));
        }

        CitySalesResp respData = new CitySalesResp();
        respData.setCitySales(citySales);

        return ResponseUtils.getNormalResp(respData);
    }

}
