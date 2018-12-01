package com.simbirsoft.testmaps.net;

import com.simbirsoft.testmaps.entities.BaseResponse;
import com.simbirsoft.testmaps.entities.MarkerEntity;
import com.simbirsoft.testmaps.entities.TeamData;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRestService {
    @GET("/quest_api/set_my_position")
    Flowable<BaseResponse<TeamData>>  setMyPosition(@Query("uid_team") String team, @Query("longitude") double longitude, @Query("latitude") double latitude);

    @GET("/quest_api/get_markers")
    Flowable<BaseResponse<List<MarkerEntity>>> getMarkers(@Query("uid_team") String team);

    @GET("/quest_api/take_marker")
    Flowable<BaseResponse> takeMarker(@Query("uid_team") String team, @Query("key_marker") String keyMarker);

    //TODO: Добавить запрос для получения подсказок http://139.59.129.2/quest_api/get_hints?uid_team=[номер_команды]
}