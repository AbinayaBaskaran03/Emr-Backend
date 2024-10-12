package com.emrheathgroup.backend.Helper;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class StaticConstants {

    public static final HashMap<Integer, HttpStatus> statusCodes = new HashMap<>(){{
       put(200,HttpStatus.OK);
       put(201, HttpStatus.CREATED);
       put(401, HttpStatus.UNAUTHORIZED);
       put(404, HttpStatus.NOT_FOUND);
       put(409, HttpStatus.CONFLICT);
       put(500, HttpStatus.INTERNAL_SERVER_ERROR);
       put(400, HttpStatus.BAD_REQUEST); 

    }};

}
