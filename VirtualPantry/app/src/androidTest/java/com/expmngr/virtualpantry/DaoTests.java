package com.expmngr.virtualpantry;

import android.content.Context;

import com.expmngr.virtualpantry.Database.DataAccessObjects.FoodDAO;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.Database.FoodDatabase;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DaoTests {
    private FoodDAO foodDAO;
    private FoodDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, FoodDatabase.class).build();
        foodDAO = db.foodDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void addGet() throws Exception {
        Food food = new Food();
        food.setName("Apple");
        foodDAO.addFood(food);
        List<Food> byName = foodDAO.getFood();
        assertEquals("food returns apple",byName.get(0).getName(), "Apple");
    }

    @Test
    public void getLocationFood() throws Exception {
        Food food = new Food();
        food.setName("Apple");
        food.setLocation("Pantry");

        Food food2 = new Food();
        food2.setName("Banana");
        food2.setLocation("Fridge");

        foodDAO.addFood(food);
        foodDAO.addFood(food2);
        List<Food> pantryFood = foodDAO.getPantryFood();
        //ensure pantry not empty
        assertTrue("Pantry should contain one food",pantryFood.size() == 1);
        assertEquals("food should be Apple",pantryFood.get(0).getName(), "Apple");
        //assertThat(pantryFood.get(0), equalTo(food));
    }

    @Test
    public void getExpired() throws Exception {
        Food food = new Food();
        food.setName("Apple");
        food.setIsExpired(true);

        Food food2 = new Food();
        food2.setName("Banana");
        food2.setIsExpired(false);

        foodDAO.addFood(food);
        foodDAO.addFood(food2);
        List<Food> expired = foodDAO.getExpiredFood();
        assertTrue("Pantry should contain one food",expired.size() == 1);
        assertEquals("food should be Apple",expired.get(0).getName(), "Apple");
    }

    @Test
    public void deleteAll() throws Exception {
        Food food = new Food();
        food.setName("Apple");

        Food food2 = new Food();
        food2.setName("Banana");

        foodDAO.addFood(food);
        foodDAO.addFood(food2);
        List<Food> foods = foodDAO.getFood();
        assertTrue("Pantry should contain one food",foods.size() == 2);

        foodDAO.deleteAll();
        foods = foodDAO.getFood();
        assertTrue("Pantry should be empty after deleteAll",foods.size() == 0);
    }
}
