package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date:2020-10-03
 * author:zhangxiaoshuai
 */
@Service
public class SpecificationService {


    @Autowired
    private SpecGroupMapper groupMapper;
    @Autowired
    private SpecParamMapper paramMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        //查询条件
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        //查询
        List<SpecGroup> list = groupMapper.select(group);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnums.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    public List<SpecParam> queryParamList(Long cid, Long gid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        List<SpecParam> list = paramMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

//

    public void AddParam(SpecParam specParam) {
        int i = this.paramMapper.insert(specParam);
        if (i == 0) {
            throw new LyException(ExceptionEnums.ADD_SPEC_PARAM_ERROR);
        }
    }

    public void UpdateSpecParm(SpecParam specParam) {

        int i = this.paramMapper.updateByPrimaryKey(specParam);
        if (i == 0) {
            throw new LyException(ExceptionEnums.ADD_SPEC_PARAM_ERROR);
        }
    }

    public void deleteParamById(Long id) {
        SpecParam specParam = new SpecParam();
        specParam.setId(id);
        int i = this.paramMapper.delete(specParam);
        if (i == 0) {
            throw new LyException(ExceptionEnums.ADD_SPEC_PARAM_ERROR);
        }
    }

    public void AddGroup(SpecGroup specGroup) {
        int i = this.groupMapper.insert(specGroup);
        if (i == 0) {
            throw new LyException(ExceptionEnums.ADD_SPEC_GROUP_ERROR);
        }
    }

    public void UpdateSpecGroup(SpecGroup specGroup) {
        int i = this.groupMapper.updateByPrimaryKey(specGroup);
        if (i == 0) {
            throw new LyException(ExceptionEnums.UPDATE_SPEC_GROUP_ERROR);
        }
    }

    public void DeleteGroupById(Long id) {
        SpecGroup group = new SpecGroup();
        group.setId(id);
        int i = this.groupMapper.delete(group);
        if (i == 0) {
            throw new LyException(ExceptionEnums.DELETE_SPEC_GROUP_ERROR);
        }
    }

    /**
     * 查询出分类下的规格组和对应的参数
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> querySpecsByCid(Long cid) {
        //查询分类下的规格组
        List<SpecGroup> specGroups = queryGroupByCid(cid);

        //查询当前分类下的参数
        List<SpecParam> params = queryParamList(cid, null, null, null);
        //将规格参数变成map，map的key是规格组id,map的值是组下所有参数
        Map<Long, List<SpecParam>> map = new HashMap<>();

        for (SpecParam param : params) {
            if (!map.containsKey(param.getGroupId())) {
                //这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(), new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        }
        //填充param到group
        for (SpecGroup group : specGroups) {
            group.setParams(map.get(group.getId()));
        }
        return specGroups;
    }
//    public List<SpecGroup> querySpecsByCid(Long cid) {
//        // 查询规格组
//        List<SpecGroup> groups = this.queryGroupByCid(cid);
//        SpecParam param = new SpecParam();
//        groups.forEach(g -> {
//            // 查询组内参数
//            g.setParams(this.queryParamList(null,g.getId(), null,null));
//        });
//
//        System.out.println("规格组数量：----------------"+groups.size());
//        return groups;
//    }
}
