/*  Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by satyabra on 11/17/15.
 */
public class ObjParseSingletonTest extends ActivityInstrumentationTestCase2 {

    public ObjParseSingletonTest() {
        super(ObjParseSingleton.class);
    }

    public void testEdit() {

        String id1 = "a";
        String id2 = "b";

        String user1 = "Food?";
        String user2 = "A very merry berry!";

        User a = new User();
        a.setUserName(user1);

        User b = new User();
        b.setUserName(user2);

        ObjParseSingleton.getInstance().addObject(id1, a);
        ObjParseSingleton.getInstance().addObject(id2, b);

        assertTrue(ObjParseSingleton.getInstance().keywordExists(id1));
        assertTrue(ObjParseSingleton.getInstance().keywordExists(id2));

        assertTrue(ObjParseSingleton.getInstance().objectExist(a));
        assertTrue(ObjParseSingleton.getInstance().objectExist(b));

        assertEquals(user1, ((User) ObjParseSingleton.getInstance().getObject(id1)).getUserName());
        assertTrue(ObjParseSingleton.getInstance().objectExist(a));

        assertEquals(user1, ((User) ObjParseSingleton.getInstance().popObject(id1)).getUserName());
        assertFalse(ObjParseSingleton.getInstance().objectExist(a));
        assertTrue(ObjParseSingleton.getInstance().objectExist(b));
    }


}
