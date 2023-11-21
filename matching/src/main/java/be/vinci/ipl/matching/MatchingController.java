package be.vinci.ipl.matching;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling matching operations.
 */
@RestController
@RequestMapping("/matching")
public class MatchingController {

  private final MatchingService matchingService;

  /**
   * Constructor for MatchingController.
   *
   * @param matchingService The MatchingService instance to handle matching operations.
   */
  public MatchingController(MatchingService matchingService) {
    this.matchingService = matchingService;
  }

  /**
   * Triggers the matching process for a specified ticker.
   *
   * @param ticker The ticker symbol for the financial instrument.
   * @return ResponseEntity indicating the result of the matching process.
   */
  @PostMapping("/trigger/{ticker}")
  public ResponseEntity<String> triggerMatching(@PathVariable String ticker) {
    boolean matchingSuccess = matchingService.matchOrdersByTicker(ticker);

    if (matchingSuccess) {
      return new ResponseEntity<>("Matching process completed successfully.", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Error during the matching process.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}