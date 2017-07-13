package com.brook.example;

import lombok.extern.log4j.Log4j2;

/**
 * Hello world!
 *
 */
@Log4j2
public class App 
{
    public static void main( String[] args )
    {
       log.info(callClazz() );
    }

    public static String callClazz(){
        return new Exception().getStackTrace()[1].getClassName();
    }
}
