package com.example.myprojectdemo.mproot.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.Delete;
import com.baomidou.mybatisplus.core.injector.methods.DeleteBatchByIds;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.core.injector.methods.DeleteByMap;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.baomidou.mybatisplus.core.injector.methods.SelectBatchByIds;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.core.injector.methods.SelectByMap;
import com.baomidou.mybatisplus.core.injector.methods.SelectCount;
import com.baomidou.mybatisplus.core.injector.methods.SelectList;
import com.baomidou.mybatisplus.core.injector.methods.SelectMaps;
import com.baomidou.mybatisplus.core.injector.methods.SelectMapsPage;
import com.baomidou.mybatisplus.core.injector.methods.SelectObjs;
import com.baomidou.mybatisplus.core.injector.methods.SelectPage;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义sql注入器
 *
 * @author Joon
 * @date 2023-03-17 15:06
 */
@Slf4j
public class CustomerSqlInjector extends AbstractSqlInjector {

    /**
     * <p>
     * 获取 注入的方法
     * </p>
     *
     * @param mapperClass 当前mapper
     * @return 注入的方法集合
     * @since 3.1.2 add  mapperClass
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo info) {
        ArrayList<AbstractMethod> methods = new ArrayList<>(info.havePK() ? 22 : 17);
        methods.add(new Insert());
        methods.add(new Delete());
        methods.add(new DeleteByMap());
        methods.add(new Update());
        methods.add(new SelectByMap());
        methods.add(new SelectCount());
        methods.add(new SelectMaps());
        methods.add(new SelectMapsPage());
        methods.add(new SelectObjs());
        methods.add(new SelectList());
        methods.add(new SelectPage());
        methods.add(new ListJson());
        methods.add(new Join("join"));
        methods.add(new Join("joinList"));
        methods.add(new Join("joinPage"));
        methods.add(new JoinJson("joinJson"));
        methods.add(new JoinJson("joinJsonList"));
        methods.add(new JoinJson("joinJsonPage"));
        if (info.havePK()) {
            methods.add(new DeleteById());
            methods.add(new DeleteBatchByIds());
            methods.add(new UpdateById());
            methods.add(new SelectById());
            methods.add(new SelectBatchByIds());
        } else {
            log.warn("{} ,Not found @TableId annotation,Cannot use Mybatis-Plus 'xxById' Method", info.getEntityType());
        }
        return methods;
    }
}
