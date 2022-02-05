package com.comp3095.recipe_project.controllers;

class A

{

    protected char c = 'A';

    char getValue()

    {

        return c;

    }

}



class B extends A

{

    protected char c = 'B';

    char getValue()

    {

        return c;

    }

    char getSuperValue()

    {

        return super.c;

    }

}
class Test

{

    public static void main(String[] args)

    {

        A a = new B();

        B b = new B();



        System.out.println(a.c + " " + a.getValue()

                + " " + b.getValue() + " " + b.getSuperValue());

    }

}
