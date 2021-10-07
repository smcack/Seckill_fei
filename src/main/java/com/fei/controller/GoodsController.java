package com.fei.controller;

import com.fei.pojo.User;
import com.fei.service.IGoodsService;
import com.fei.service.ISeckillGoodsService;
import com.fei.service.IUserService;
import com.fei.vo.DetailVo;
import com.fei.vo.GoodsVo;
import com.fei.vo.ResBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @program: Seckill
 * @description: 商品控制类
 * @author: fei
 * @create: 2021-09-29 16:46
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
     private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    /**
     * 跳转商品列表
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/toList")
    public String toList(Model model,User user) {

        model.addAttribute("goodsList",goodsService.findGoodsVo());
        model.addAttribute("user", user);
        return "goodsList";
    }

    /**
     * 跳转商品详情页
     * @param GoodsId
     * @return
     */
    @RequestMapping(value = "/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable Long goodsId){
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀还未开始
        if (nowDate.before(startDate)) {
            //startDate.getTime()是毫秒
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)) {
            //	秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }
//        DetailVo detailVo = new DetailVo();
//        detailVo.setUser(user);
//        detailVo.setGoodsVo(goodsVo);
//        detailVo.setRemainSeconds(remainSeconds);
//        detailVo.setSecKillStatus(secKillStatus);
//        return ResBean.success(detailVo);

        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("goods", goodsService.findGoodsVoById(goodsId));
        return "goodsDetail";

    }
}
