package com.brain.util;

import com.brain.R;

import java.util.ArrayList;

public class Util {

    public static int[] getTabIcon = {
            R.drawable.ic_home_public,
            R.drawable.ic_chat,
            R.drawable.ic_profile
    };

    public static <E> boolean containsInstance(ArrayList<E> list, Class<? extends E> clazz) {
        return list.stream().anyMatch(clazz::isInstance);
    }
}
