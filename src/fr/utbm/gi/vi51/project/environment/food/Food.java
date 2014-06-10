/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utbm.gi.vi51.project.environment.food;

import org.janusproject.jaak.envinterface.perception.EnvironmentalObject;

/**
 *
 * @author Benjamin
 */
public class Food extends EnvironmentalObject
{
    private int _price;
    private int _quantity;
    
    private int _duration; // Durée pendant laquelle on n'a plus faim quand on prend une bouchée en secondes
    
    public Food(int price, int quantity, int durationInSec)
    {
        super(new Object());
        this._price = price;
        this._quantity = quantity;
        this._duration = durationInSec;
    }
    
    
    public boolean canEat()
    {
        return _quantity > 0;
    }
    
    // retourne la durée pendant laquelle on n'a plus faim avec cette bouchée
    public int eat()
    {
        if(_quantity > 0)
        {
            _quantity--;
            return _duration;
        }
        //return _duration * 60; // On augmente la valeur de la dernière bouchée ?
        return 0;
    }
    
    
    public int getPrice() {
        return _price;
    }

    public int getQuantity() {
        return _quantity;
    }
    
    
    
    
}
