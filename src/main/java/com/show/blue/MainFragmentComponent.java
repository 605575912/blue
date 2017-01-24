package com.show.blue;


import dagger.Subcomponent;

/**
 * Created by niuxiaowei on 16/3/20.
 */
@Subcomponent
public interface MainFragmentComponent {
    void inject(ChooseItem mainFragment);
}
