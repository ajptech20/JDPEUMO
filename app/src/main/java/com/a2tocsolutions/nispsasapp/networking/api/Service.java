package com.a2tocsolutions.nispsasapp.networking.api;

import com.a2tocsolutions.nispsasapp.model.ArticleResponse;
import com.a2tocsolutions.nispsasapp.model.ChildResponse;
import com.a2tocsolutions.nispsasapp.model.ExtinguisherResponse;
import com.a2tocsolutions.nispsasapp.model.FaceVerifyResponse;
import com.a2tocsolutions.nispsasapp.model.HotZoneDetails;
import com.a2tocsolutions.nispsasapp.model.LoginResponse;
import com.a2tocsolutions.nispsasapp.model.ResponseTransporter;
import com.a2tocsolutions.nispsasapp.model.UseRscDetails;
import com.a2tocsolutions.nispsasapp.model.VerifyDetails;
import com.a2tocsolutions.nispsasapp.model.VerifyResponse;
import com.a2tocsolutions.nispsasapp.model.VideoReportDetails;
import com.a2tocsolutions.nispsasapp.networking.Routes;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Service {

    // Covid Section
    @FormUrlEncoded
    @POST(Routes.COVID_ALERT + "/pushCovidAlerts")
    Call<Void> covidAlert(@Field("reporter") String reporter, @Field("lat") String lat, @Field("lng") String lng, @Field("type") String type);

    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/pushrapeviolenceMediaAlerts")
    Call<Void> uploadRape(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type);

    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/pushcolapsBuildingAlerts")
    Call<Void> uploadCommen(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type);

    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/pushblockedDrainagAlerts")
    Call<Void> uploadComme(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type);

    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/pushseesayMediaAlerts")
    Call<Void> uploadComment(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type, @Part("reportedto") RequestBody reportedto);

    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/pushseesayvid")
    Call<Void> uploadVideo(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type, @Part("reportedto") RequestBody reportedto);


    @FormUrlEncoded
    @POST(Routes.PWD_ALERT + "/pushPwdAlert")
    Call<Void> crimeAlerter(@Field("reporter") String reporter, @Field("lat") String lat, @Field("lng") String lng, @Field("type") String type);

    @FormUrlEncoded
    @POST(Routes.LOGIN + "/registerUser")
    Call<LoginResponse> loginUser(@Field("username") String username, @Field("phonenumber") String phonenumber, @Field("token") String token);

    @POST(Routes.FIRE_ALERT + "/pushFireAlert/" + "{phonenumber}/" + "{lat}/" + "{lng}/" + "Fire")
    Call<Void> fireAlert(@Path("phonenumber") String phonenumber, @Path("lat") String lat, @Path("lng") String lng);

    @POST(Routes.MEDICAL_ALERT + "/pushMedicalAlert/" + "{phonenumber}/" + "{lat}/" + "{lng}/" + "Mobile" )
    Call<Void> medicalAlert(@Path("phonenumber") String phonenumber, @Path("lat") String lat, @Path("lng") String lng);

    @FormUrlEncoded
    @POST(Routes.FLOOD_ALERT + "/pushFloodAlerts")
    Call<Void> floodAlert(@Field("reporter") String reporter, @Field("lat") String lat, @Field("lng") String lng, @Field("type") String type);

    @FormUrlEncoded
    @POST(Routes.POST_REP_URL + "/pushLivePost")
    Call<Void> goingLivePost(@Field("reporter") String reporter, @Field("lat") String lat, @Field("lng") String lng, @Field("officername") String repname, @Field("state") String repstate, @Field("lga") String replga, @Field("typerep") String mreptype, @Field("repcomment") String comment, @Field("type") String type);

    @FormUrlEncoded
    @POST(Routes.POST_REP_URL + "/pushShortsPost")
    Call<Void> ShortVidPost(@Field("reporter") String reporter, @Field("lat") String lat, @Field("lng") String lng, @Field("officername") String repname, @Field("state") String repstate, @Field("lga") String replga, @Field("typerep") String mreptype, @Field("repcomment") String comment, @Field("type") String type);

    @FormUrlEncoded
    @POST(Routes.POST_REP_URL + "/pushImagePost")
    Call<Void> ImagePostUp(@Field("reporter") String reporter, @Field("lat") String lat, @Field("lng") String lng, @Field("officername") String repname, @Field("state") String repstate, @Field("lga") String replga, @Field("typerep") String mreptype, @Field("repcomment") String comment, @Field("type") String type);

    @FormUrlEncoded
    @POST(Routes.CRIME_ALERT + "/pushCrimeAlerts")
    Call<Void> crimeAlert(@Field("reporter") String reporter, @Field("lat") String lat, @Field("lng") String lng, @Field("type") String type, @Field("user_id") String user_id);

    @POST(Routes.PANIC_BUTTON + "/pushPanicAlert/" + "{phonenumber}/" + "{lat}/" + "{lng}/" + "Panic_Button")
    Call<Void> panicAlert(@Path("phonenumber") String phonenumber, @Path("lat") String lat, @Path("lng") String lng);

    /*@FormUrlEncoded
    @POST(Routes.ARTICLE + "/getArticle/")
    Call<ArticleResponse> article(@Field("type") String type);*/

    @FormUrlEncoded
    @POST(Routes.VERIFY_FIRE_EXTINGUISHER + "/VerifyFireCode")
    Call<ExtinguisherResponse> verifyExtinguisher(@Field("code") String code);

    @FormUrlEncoded
    @POST(Routes.VERIFY_ADDRESS + "/VerifyAddress")
    Call<ExtinguisherResponse> verifyAddress(@Field("phonenumber") String phone);

    @FormUrlEncoded
    @POST(Routes.VERIFY_NATIONAL + "/VerifyNational")
    Call<ExtinguisherResponse> verifyNational(@Field("phonenumber") String phone);

    @FormUrlEncoded
    @POST(Routes.VERIFY_TRANSPORTER + "/verifyDriver")
    Call<ResponseTransporter> transporter(@Field("encode") String encode);

    @FormUrlEncoded
    @POST(Routes.VERIFY_ALGON + "/VerifyAlgon")
    Call<ResponseTransporter> algon(@Field("algoncode") String algoncode, @Field("type") String type);

    @Multipart
    @POST(Routes.FACE_VERIFY + "/VerifyDomesticArtisan")
    Call<FaceVerifyResponse> faceupload(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(Routes.FACE_VERIFY + "/RateDomesticArtisan")
    Call<VerifyResponse> uploadRate(@Field("empcomment") String empcomment, @Field("empname") String empname, @Field("phonenumber") String phonenumber, @Field("emprate") String emprate, @Field("empnumber") String empnumber) ;

    @Multipart
    @POST(Routes.FACE_VERIFY + "/verifyChild")
    Call <ChildResponse> childupload(@Part MultipartBody.Part file);

    @Multipart
    @POST(Routes.FACE_VERIFY + "/verifyHajj")
    Call <ChildResponse> hajjupload(@Part MultipartBody.Part file);


    @Multipart
    @POST(Routes.FACE_VERIFY + "/verifyNurseMidwifery")
    Call <ChildResponse> nurseupload(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(Routes.FACE_VERIFY + "/VerifyPlateNumber")
    Call<ExtinguisherResponse> plateupload(@Field("platenum") String platenum);

    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/cirpReport")
    Call<Void> cirpComment(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type, @Part("department") RequestBody department, @Part("school") RequestBody school);


    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/pushMediaAlerts")
    Call<Void> uploadComment(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type);

	@Multipart
    @POST(Routes.UPLOAD_MEDIA + "/pushMediaAlert")
    Call<Void> sendComment(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file, @Path("phonenumber") String phonenumber, @Path("lat") String lat, @Path("lng") String lng);

    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/pushDiscrimMediaAlerts")
    Call<Void> uploadComments(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type);

    @Multipart
    @POST(Routes.LAGOS_MEDIA)
    Call<Void> lagComment(@Part("comment") RequestBody comment, @Part MultipartBody.Part file, @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("type") RequestBody type);

    @Multipart
    @POST(Routes.UPLOAD_MEDIA + "/PubComplaint")
    Call<Void> uploadComplaint(@Part("reporteremail") RequestBody reporteremail, @Part("title") RequestBody title, @Part("fullreport") RequestBody fullreport, @Part("comemail") RequestBody comemail, @Part("comname") RequestBody comname, @Part("comphone") RequestBody comphone, @Part("tamount") RequestBody tamount, @Part("tdate") RequestBody tdate,
                               @Part("reporter") RequestBody reporter, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng, @Part("sendername") RequestBody sendername, @Part MultipartBody.Part file);

    @Multipart
    @POST("RegSchool")
    Call<Void> nsreg(@Part("type") RequestBody typer, @Part("name") RequestBody name, @Part("state") RequestBody state, @Part("contactname") RequestBody contactname, @Part("contactphone") RequestBody contactphone, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng,@Part("address") RequestBody address, @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST(Routes.VERIFY_SECURITY_CODE)
    Call<VerifyDetails> securitycode(@Field("servicecode") String servicecode);

    @Multipart
    @POST(Routes.VERIFY_SECURITY_FACE)
    Call <ChildResponse> securityface(@Part MultipartBody.Part file);

    @Multipart
    @POST(Routes.FACE_VERIFY)
    Call <ChildResponse> security(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(Routes.VERIFY_HUNTER)
    Call<VerifyDetails> verifyHunter(@Field("servicecode") String servicecode);

    @FormUrlEncoded
    @POST(Routes.UPLOAD_AGRIC)
    Call<VerifyResponse> uploadAgric(@Field("fullname") String fullname, @Field("sex") String sex, @Field("phoneno") String phoneno, @Field("plateno") String plateno, @Field("encode") String encode, @Field("productinfo") String productinfo, @Field("cugname") String cugname, @Field("satcid") String satcid, @Field("deptpoint") String deptpoint,
                                     @Field("timedept") String timedept, @Field("destpoint") String destpoint, @Field("timearrive") String timearrive) ;

    @FormUrlEncoded
    @POST(Routes.VERIFY_PSID)
    Call<VerifyDetails> recievcall(@Field("callid") String callid);

    @FormUrlEncoded
    @POST(Routes.Fetch_Live_Videos)
    Call<VideoReportDetails> getlivevideos(@Field("id") String callid);

    @FormUrlEncoded
    @POST(Routes.Fetch_Hot_zones)
    Call<HotZoneDetails> gethotzones(@Field("vidtype") String callid);

    @FormUrlEncoded
    @POST(Routes.Fetch_Use_Res)
    Call<UseRscDetails> getuseresoutce(@Field("vidtype") String callid);

    @FormUrlEncoded
    @POST(Routes.LOGIN + "/Registerpolinunit")
    Call<LoginResponse> pollinUnit(@Field("officerphone") String officerphone, @Field("state") String state, @Field("lga") String lga, @Field("polinunit") String pollinunit);


    @Multipart
    @POST(Routes.UPLOAD_USER_DATA + "/RegisterpolinunitUpload")
    Call<Void> updateUserdata( @Part("officerphone") RequestBody officerphone, @Part MultipartBody.Part file, @Part("state") RequestBody states, @Part("lga") RequestBody lga, @Part("officername") RequestBody officername, @Part("town") RequestBody town);


    @FormUrlEncoded
    @POST(Routes.ARTICLE + "/Nemanews/")
    Call<ArticleResponse> article(@Field("type") String type);
}
