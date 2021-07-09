/**
 * Copyright (c) 2019, INTech.
 * this file is part of INTech's HighLevel.
 *
 * INTech's HighLevel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INTech's HighLevel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with it.  If not, see <http://www.gnu.org/licenses/>.
 **/

package orders.order;

import lowlevel.order.Order;
import lowlevel.order.OrderBuilder;
import lowlevel.order.OrderWithArgument;
import static utils.communication.Formatting.*;

/**
 * Tous les ordres envoyés au LL concernant les mouvements du robot
 *
 * @author yousra, jglrxavpok,Ohm-T
 */
public class MotionOrders {
    /**Avancer*/
    public static final OrderWithArgument MoveLengthwise = OrderBuilder.createWithArgs("d", INTEGER, BOOLEAN);
    /**Aspirer*/
    public static final OrderWithArgument Suck=OrderBuilder.createWithArgs("sk",INTEGER,INTEGER);
    public static final OrderWithArgument Suckall=OrderBuilder.createWithArgs("ska",INTEGER);
    /**Marteaux*/
    public static final OrderWithArgument Hammers=OrderBuilder.createWithArgs("hmrs",INTEGER);
    public static final OrderWithArgument Hammer=OrderBuilder.createWithArgs("hm",INTEGER,INTEGER,INTEGER,INTEGER,INTEGER);
    /** Lever bras */
    public static final OrderWithArgument Flag=OrderBuilder.createWithArgs("fl",INTEGER);;
    /** Tourner */
    public static final OrderWithArgument Turn = OrderBuilder.createWithArgs("t", FLOAT5);
    /** Aller jusqu'à un point */
    public static final OrderWithArgument MoveToPoint = OrderBuilder.createWithArgs("goto", INTEGER, INTEGER);
    public static final  OrderWithArgument Arm = OrderBuilder.createWithArgs("arm",INTEGER,INTEGER);
    /**S'arrêter*/
    public static final Order Stop = OrderBuilder.createSimple("stop");
    /** On force l'arrêt */
    public static final Order ForceStop = OrderBuilder.createSimple("sstop");
    /** Couper l'asserv en rotation */
    public static final Order DisableRotationControl = OrderBuilder.createSimple("cr0");
    /** Activer l'asserv en rotation */
    public static final Order EnableRotationControl = OrderBuilder.createSimple("cr1");



}
