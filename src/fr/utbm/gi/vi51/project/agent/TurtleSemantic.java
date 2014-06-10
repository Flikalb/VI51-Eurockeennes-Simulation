/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utbm.gi.vi51.project.agent;

import java.lang.ref.WeakReference;
import org.janusproject.kernel.agent.Agent;

/**
 *
 * @author Benjamin
 */
public class TurtleSemantic extends Object
{
    private WeakReference<Agent> _owner;
    
    public TurtleSemantic(Agent owner)
    {
        this._owner = new WeakReference<Agent>(owner);
    }
    
    public Agent getOwner()
    {
        return _owner.get();
    }

}
