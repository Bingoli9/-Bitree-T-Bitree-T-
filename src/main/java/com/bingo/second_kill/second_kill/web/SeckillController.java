package com.bingo.second_kill.second_kill.web;

import com.bingo.second_kill.second_kill.dto.Exposer;
import com.bingo.second_kill.second_kill.dto.SeckillExecution;
import com.bingo.second_kill.second_kill.dto.SeckillResult;
import com.bingo.second_kill.second_kill.entity.Seckill;
import com.bingo.second_kill.second_kill.enums.SeckillStatEnum;
import com.bingo.second_kill.second_kill.exception.RepeatKillException;
import com.bingo.second_kill.second_kill.exception.SeckillCloseException;
import com.bingo.second_kill.second_kill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")//url:模块/资源/{}/细分
public class SeckillController
{
    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list()
    {

        //获取列表页
        List<Seckill> list=seckillService.getSeckillList();
        
        return list.toString();
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId)
    {
       

        Seckill seckill=seckillService.getById(seckillId);
        if (seckill==null)
        {
            return "不存在商品";
        }

        return seckill.toString();
    }

    //ajax ,json暴露秒杀接口的方法
    @RequestMapping(value = "/{seckillId}/exposer",
                    method = RequestMethod.GET,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId)
    {
        SeckillResult<Exposer> result;
        try{
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e)
        {
            e.printStackTrace();
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "userPhone",required = false) Long userPhone)
    {
        if (userPhone==null)
        {
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }

        try {
        	//这里改为调用存储过程
//            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
             SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            return new  SeckillResult< SeckillExecution>(true, execution);
        }catch (RepeatKillException e1)
        {
             SeckillExecution execution=new  SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new  SeckillResult< SeckillExecution>(true,execution);
        }catch (SeckillCloseException e2)
        {
             SeckillExecution execution=new  SeckillExecution(seckillId, SeckillStatEnum.END);
            return new  SeckillResult< SeckillExecution>(true,execution);
        }
        catch (Exception e)
        {
             SeckillExecution execution=new  SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new  SeckillResult< SeckillExecution>(true,execution);
        }
    }

    //获取系统时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public  SeckillResult<Long> time()
    {
        Date now=new Date();
        return new  SeckillResult<Long>(true,now.getTime());
    }
}
