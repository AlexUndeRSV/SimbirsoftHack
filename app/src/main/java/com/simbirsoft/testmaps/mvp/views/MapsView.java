package com.simbirsoft.testmaps.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.simbirsoft.testmaps.entities.MarkerEntity;
import com.simbirsoft.testmaps.entities.TeamData;

import java.util.List;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface MapsView extends MvpView {
    void onTeamData(TeamData data);
    void onMarkersLoad(List<MarkerEntity> markers);
    void onTakeMarker(String msg);
    void onTakeMessageHints(String messages);
    //TODO: добавить метод, вызываемый, после загрузки подсказок
    void onError(Throwable t);
}
