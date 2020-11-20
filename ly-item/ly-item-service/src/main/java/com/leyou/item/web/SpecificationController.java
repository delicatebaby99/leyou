package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * date:2020-10-03
 * author:zhangxiaoshuai
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specService;

    /**
     * 根据分类id获取规格组
     *
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(
            @PathVariable("cid") Long cid

    ) {

        return ResponseEntity.ok(specService.queryGroupByCid(cid));
    }

    /**
     * 根据(分类，规格组，搜索条件)获取规格参数
     *
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamList(
            @RequestParam(value = "cid" ,required = false) Long cid,
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "searching",required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic
            ) {

        return ResponseEntity.ok(specService.queryParamList(cid,gid,searching,generic));
    }

    /**
     * 新增规格组
     *
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> AddGroup(SpecGroup specGroup) {

        this.specService.AddGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格组
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> UpdateGroup(SpecGroup specGroup) {
        this.specService.UpdateSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 删除规格组通过id
     * @param id
     * @return
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> DeleteGroup(@PathVariable("id")Long id){
        this.specService.DeleteGroupById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 新增规格参数
     *
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> AddParam(SpecParam specParam) {

        this.specService.AddParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格参数
     *
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> UpdateParamByGid(SpecParam specParam) {
        specService.UpdateSpecParm(specParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 删除规格参数
     *
     * @param id
     * @return
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> DeleteParamById(@PathVariable("id") Long id) {
        this.specService.deleteParamById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据分类查询规格组以及组内参数
     * @param cid
     * @return
     */
    @GetMapping("/group")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@RequestParam("cid") Long cid){
        List<SpecGroup> list = this.specService.querySpecsByCid(cid);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }





}
