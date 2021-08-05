package com.barcode.examination.connection;

import com.barcode.examination.model.AllStudentDataResp;
import com.barcode.examination.model.BarCodeMatchBody;
import com.barcode.examination.model.BarcodeResponse;
import com.barcode.examination.model.BypassAuthResponse.BypassAuthAPIResponse;
import com.barcode.examination.model.GetAbsentCandidatesResponse.GetAbsentCandidatesAPIResponse;
import com.barcode.examination.model.GetCandidateDataForAnyCentreResponse.GetCandidateDataForAnyCentreAPIResponse;
import com.barcode.examination.model.GetCandidateDataResponse.GetCandidateDataAPIResponse;
import com.barcode.examination.model.GetPresentCandidatesResponse.GetPresentCandidatesAPIResponse;
import com.barcode.examination.model.LoginBody;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.model.LogoutResponse.LogoutAPIResponse;
import com.barcode.examination.model.RegisterFaceResponse.RegisterFaceAPIResponse;
import com.barcode.examination.model.RegisterIrisResponse.RegisterIrisAPIResponse;
import com.barcode.examination.model.SaveDataToServer;
import com.barcode.examination.model.SaveDataToServerResponse.SaveDataToServerAPIResponse;
import com.barcode.examination.model.ScanWithFaceResponse.ScanWithFaceAPIResponse;
import com.barcode.examination.model.StudentAvailabilityBody;
import com.barcode.examination.model.StudentAvailabilityResp;
import com.barcode.examination.model.SyncDataResponse.SyncDataAPIResponse;
import com.barcode.examination.model.UpdateCandidateCentreBody;
import com.barcode.examination.model.UpdateCandidateCentreResponse.UpdateCandidateCentreAPIResponse;
import com.barcode.examination.model.UpdateOccurrenceBody;
import com.barcode.examination.model.UpdateOccurrenceResponse.UpdateOccurrenceAPIResponse;
import com.barcode.examination.model.VerifyFaceResponse.VerifyFaceAPIResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiDataService {

    // login api
    @POST("/rest/centerUser/login")
    Call<LoginAPIResponse> GetValidLogin(@Body LoginBody body);

    // logout api
    @GET("/rest/centerUser/logout")
    Call<LogoutAPIResponse> Logout(@Header("access_token") String token);

    // sync candidates list api
    @GET("/rest/centerUser/syncCandidatesList")
    Call<SyncDataAPIResponse> SyncCandidatesList(@Header("access_token") String token);

    // get present student data
    @GET("/rest/centerUser/getPresentCandidates")
    Call<GetPresentCandidatesAPIResponse> GetPresentCandidatesList(@Header("access_token") String token, @Query("page") String page,@Query("limit") String limit);

    // get absent student data
    @GET("/rest/centerUser/getAbsentCandidates")
    Call<GetAbsentCandidatesAPIResponse> GetAbsentCandidatesList(@Header("access_token") String token, @Query("page") String page, @Query("limit") String limit);

    // save local data to server
    @POST("/rest/centerUser/saveDataToServer")
    Call<SaveDataToServerAPIResponse> saveDataToServer(@Header("access_token") String token,@Body SaveDataToServer body);

    // get student data
    @GET("/rest/centerUser/getCandidateData")
    Call<GetCandidateDataAPIResponse> GetCandidateData(@Header("access_token") String token, @Query("rollNumber") String rollNumber);

    // get student data for any centre but same shift and date
    @GET("/rest/centerUser/getCandidateDataFromAnyCentre")
    Call<GetCandidateDataForAnyCentreAPIResponse> GetCandidateDataForAnyCentre(@Header("access_token") String token, @Query("rollNumber") String rollNumber);

    // update candidate occurrance api
    @POST("/rest/centerUser/updateCandidateOccurrance")
    Call<UpdateOccurrenceAPIResponse> UpdateCandidateOccurrence(@Header("access_token") String token,@Body UpdateOccurrenceBody body);

    // update candidate centre api
    @POST("/rest/centerUser/updateCandidateCentreCode")
    Call<UpdateCandidateCentreAPIResponse> UpdateCandidateCentre(@Header("access_token") String token, @Body UpdateCandidateCentreBody body);

    // bypass candidate authentication api
    @Multipart
    @POST("/rest/centerUser/bypassCandidateAuth")
    Call<BypassAuthAPIResponse> BypassCandidateAuth(@Header("access_token") String token, @Part("rollNumber") RequestBody rollNumber, @Part("authType") RequestBody authType, @Part("reason") RequestBody reason , @Part MultipartBody.Part image);

    // register candidate face api
    @Multipart
    @POST("/rest/centerUser/registerCandidateFace")
    Call<RegisterFaceAPIResponse> RegisterCandidateFace(@Header("access_token") String token, @Part("rollNumber") RequestBody rollNumber, @Part MultipartBody.Part image);

    // register candidate iris api
    @Multipart
    @POST("/rest/centerUser/registerCandidateIris")
    Call<RegisterIrisAPIResponse> RegisterCandidateIris(@Header("access_token") String token, @Part("rollNumber") RequestBody rollNumber, @Part MultipartBody.Part image, @Part MultipartBody.Part image2);

    // verify candidate face api
    @Multipart
    @POST("/rest/centerUser/verifyCandidateFace")
    Call<VerifyFaceAPIResponse> VerifyCandidateFace(@Header("access_token") String token, @Part("rollNumber") RequestBody rollNumber, @Part MultipartBody.Part image);

    // scan with face candidate api
    @Multipart
    @POST("/rest/centerUser/scanFaceForData")
    Call<ScanWithFaceAPIResponse> ScanFaceForData(@Header("access_token") String token, @Part MultipartBody.Part image);


    // bar code match api
    @POST("/barmatch")
    Call<BarcodeResponse> barCodeMatch(@Body BarCodeMatchBody body);

    // get all student data
    @GET("/datalist")
    Call<AllStudentDataResp> getAllStudentData();

//    // save local data to server
//    @POST("/hit")
//    Call<SaveDataToServerResp> saveDataToServer(@Body SaveDataToServer body);

    // absent student data
    @POST("/Absent")
    Call<StudentAvailabilityResp> absent(@Body StudentAvailabilityBody body);

    // present student data
    @POST("/Present")
    Call<StudentAvailabilityResp> present(@Body StudentAvailabilityBody body);
}
