package com.example.training_manager.Controller;


import com.example.training_manager.Dto.Report.TonReportGetDto;
import com.example.training_manager.Service.Report.FetchTonAllExerciseTonsByCustomerId;
import com.example.training_manager.Service.Report.FetchTonIndividualWorkoutByCustomerId;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reports")
@CrossOrigin(origins = "http://localhost:8100")
public class ReportController {

    private final FetchTonAllExerciseTonsByCustomerId fetchTonAllExerciseTonsByCustomerid;
    private final FetchTonIndividualWorkoutByCustomerId fetchTonIndividualWorkoutByCustomerId;

    ReportController(FetchTonAllExerciseTonsByCustomerId fetchTonAllExerciseTonsByCustomerId,
                     FetchTonIndividualWorkoutByCustomerId fetchTonIndividualWorkoutByCustomerId){
        this.fetchTonAllExerciseTonsByCustomerid = fetchTonAllExerciseTonsByCustomerId;
        this.fetchTonIndividualWorkoutByCustomerId = fetchTonIndividualWorkoutByCustomerId;
    }
    @GetMapping("/tons/total/exercise")
    public ResponseEntity<Float> getTonsBySumAllIndividualExercises(@RequestBody TonReportGetDto tonReportGetDto, @RequestHeader("Authorization") String authHeader) throws Exception{
        try{
            return ResponseEntity.ok(fetchTonAllExerciseTonsByCustomerid.execute(tonReportGetDto, authHeader));
        } catch (Exception e){
            throw new Exception("Erro ao recuperar relatório de tonelagem total por exercício: " + e);
        }
    }

   @GetMapping("/tons/individual/workout")
    public ResponseEntity<Float> getTonsBySumOneWorkout(@RequestBody TonReportGetDto tonReportGetDto, @RequestHeader("Authorization") String authHeader) throws Exception{
        try{
            return ResponseEntity.ok(fetchTonIndividualWorkoutByCustomerId.execute(tonReportGetDto, authHeader));
        } catch (Exception e){
            throw new Exception("Erro ao recuperar relatório de tonelagem individual por treino: "  + e);
        }
   }
}
