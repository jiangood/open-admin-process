package io.github.jiangood.as.modules.flowable.dao;

import io.github.jiangood.as.modules.flowable.config.meta.ProcessMeta;

import java.util.List;

public interface IProcessMetaDao {

    List<ProcessMeta> findProcessMetaList();


}
