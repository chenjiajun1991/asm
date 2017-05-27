package com.sam.yh.clock;

import org.springframework.scheduling.annotation.Async;

import com.sam.yh.crud.exception.CrudException;


public interface CheckErrBtyClock {

@Async("asyncExecutor")	
public void fetchErr() throws CrudException;

}
