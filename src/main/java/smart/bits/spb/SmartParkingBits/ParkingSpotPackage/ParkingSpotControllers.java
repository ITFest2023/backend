package smart.bits.spb.SmartParkingBits.ParkingSpotPackage;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spot")
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
    @PatchMapping("/register")
    public ResponseEntity<String> registerParkingSpotInfo(@RequestParam(name = "uuid") UUID uuid, @RequestBody RegisterRequest registerRequest) {

        return parkingSpotServices.registerParkingSpotInfo(uuid, registerRequest);

    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteParkingSpotById(@PathVariable UUID uuid) {

        return parkingSpotServices.deleteParkingSpotById(uuid);

    }

}
