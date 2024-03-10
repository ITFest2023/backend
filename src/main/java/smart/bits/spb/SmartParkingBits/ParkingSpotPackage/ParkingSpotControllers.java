package smart.bits.spb.SmartParkingBits.ParkingSpotPackage;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spot")
@CrossOrigin(origins = "*")
public class ParkingSpotControllers {

    @Autowired
    private ParkingSpotServices parkingSpotServices;

    @PostMapping("/create")
    public ResponseEntity<String> addNewParkingSpot(@RequestBody ParkingSpotEntity parkingSpotEntity) {

        return parkingSpotServices.addNewParkingSpot(parkingSpotEntity);

    }

    @GetMapping("/fetch")
    public List<ParkingSpotEntity> getAllParkingSpotsInfo() {

        return parkingSpotServices.getAllParkingSpotsInfo();

    }

    @GetMapping("/fetch/{uuid}")
    public ParkingSpotEntity getParkingSpotInfoById(@PathVariable UUID uuid) {

        return parkingSpotServices.getParkingSpotInfoById(uuid);

    }

    @PatchMapping("/notify")
    public ResponseEntity<String> updateParkingSpotInfo(@RequestBody ParkingSpotEntity parkingSpotEntity) {
        return parkingSpotServices.notifyParkingSpotInfo(parkingSpotEntity);

    }

    record RegisterRequest(double lat, double lng) {}
    @GetMapping("/register")
    public ResponseEntity<String> registerParkingSpotInfo(@RequestParam(name = "uuid") UUID uuid, @RequestParam double lat, @RequestParam double lng) {

        return parkingSpotServices.registerParkingSpotInfo(uuid, lat, lng);

    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteParkingSpotById(@PathVariable UUID uuid) {

        return parkingSpotServices.deleteParkingSpotById(uuid);

    }

}
