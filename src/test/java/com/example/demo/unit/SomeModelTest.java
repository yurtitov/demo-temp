package com.example.demo.unit;

import com.example.demo.SomeModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SomeModelTest {

    @Test
    void plusOperationTest() {
        SomeModel sut = new SomeModel();
        int actual = sut.plusOperation(2, 3);
        assertEquals(5, actual);
    }
}