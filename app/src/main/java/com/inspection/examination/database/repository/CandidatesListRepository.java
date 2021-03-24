//package com.inspection.examination.database.repository;
//
//import android.app.Application;
//import android.os.AsyncTask;
//
//import androidx.lifecycle.LiveData;
//
//import com.inspection.examination.database.db.CandidatesDAO;
//import com.inspection.examination.database.db.CandidatesDatabase;
//import com.inspection.examination.model.SyncDataResponse.CandidatesList;
//
//import java.util.List;
//
//public class CandidatesListRepository {
//    private CandidatesDAO candidatesDAO;
//    private LiveData<List<CandidatesList>> allCandidates;
//
//    public CandidatesListRepository(Application application){
//        CandidatesDatabase database = CandidatesDatabase.getInstance(application);
//        candidatesDAO = database.candidatesDAO();
//        allCandidates = candidatesDAO.getAllData();
//    }
//
//    public void updateOccurranceForStudent(String rollNumber, int count){
//        new UpdateOccurranceForStudentAsyncTask(candidatesDAO).execute(rollNumber,count)
//    }
//
//    public int getOccurrenceForStudent(String rollNo){
//
//    }
//
//    public LiveData<CandidatesList> getStudentData(String rollNo){
//
//    }
//
//    public LiveData<List<CandidatesList>> getAllData(){
//        return allCandidates;
//    }
//
//    private static class UpdateOccurranceForStudentAsyncTask extends AsyncTask<CandidatesList,Void,Void>{
//        private CandidatesDAO candidatesDAO;
//        private UpdateOccurranceForStudentAsyncTask(CandidatesDAO candidatesDAO){
//            this.candidatesDAO=candidatesDAO;
//        }
//
//        @Override
//        protected Void doInBackground(CandidatesList... candidatesLists) {
//            candidatesDAO.updateOccurranceForStudent(candidatesLists[0].getRollNumber(),candidatesLists[0].getOccurrance());
//            return null;
//        }
//    }
//
//    private static class GetOccurranceForStudentAsyncTask extends AsyncTask<CandidatesList,Void,Void>{
//        private CandidatesDAO candidatesDAO;
//        private GetOccurranceForStudentAsyncTask(CandidatesDAO candidatesDAO){
//            this.candidatesDAO=candidatesDAO;
//        }
//
//        @Override
//        protected Void doInBackground(CandidatesList... candidatesLists) {
//            candidatesDAO.getOccurrenceForStudent(candidatesLists[0].getRollNumber());
//            return null;
//        }
//    }
//}
