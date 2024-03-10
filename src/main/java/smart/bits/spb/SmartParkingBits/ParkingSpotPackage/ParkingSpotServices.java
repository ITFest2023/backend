package smart.bits.spb.SmartParkingBits.ParkingSpotPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParkingSpotServices {

    private static long FIFTEEN_MILI = 15000;
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    public ResponseEntity<String> addNewParkingSpot(ParkingSpotEntity parkingSpotEntity) {

        UUID uuid = parkingSpotEntity.getUuid();

        if (uuid == null) {
            return ResponseEntity.notFound().build();
        }

        if (parkingSpotRepository.findById(uuid).isPresent()) {
            return ResponseEntity.badRequest().body("ESP already exists!");
        }

        parkingSpotRepository.saveAndFlush(parkingSpotEntity);
        return ResponseEntity.ok().body("Parking spot created");

    }

    public List<ParkingSpotEntity> getAllParkingSpotsInfo() {

        return parkingSpotRepository
                .findAll()
                .stream()
                .filter(e -> !e.getStatus().equals(ParkingSpotStatus.UNREGISTERED))
                .map(this::isDeviceOffline)
                .collect(Collectors.toList());
    }

    public ParkingSpotEntity getParkingSpotInfoById(UUID uuid) {

        if (uuid == null) {
            return null;
        }

        Optional<ParkingSpotEntity> parkingSpot = parkingSpotRepository.findById(uuid);
        return parkingSpot.orElse(null);

    }

    public ResponseEntity<String> deleteParkingSpotById(UUID uuid) {

        if (uuid == null) {
            return ResponseEntity.notFound().build();
        }

        if (parkingSpotRepository.findById(uuid).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        parkingSpotRepository.deleteById(uuid);

        return ResponseEntity.ok().body("Parking spot deleted!");

    }

    public ResponseEntity<String> registerParkingSpotInfo(UUID uuid, double lat, double lng) {

        if (uuid == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<ParkingSpotEntity> parkingSpotEntityOptional = parkingSpotRepository.findById(uuid);
        if (parkingSpotEntityOptional.isPresent()) {
            ParkingSpotEntity parkingSpotEntity = parkingSpotEntityOptional.get();
            parkingSpotEntity.setLat(lat);
            parkingSpotEntity.setLng(lng);
            parkingSpotEntity.setStatus(ParkingSpotStatus.REGISTERED);

            parkingSpotRepository.saveAndFlush(parkingSpotEntity);

            return ResponseEntity.ok().body("ESP registered!");
        }

        return ResponseEntity.notFound().build();

    }

    public ResponseEntity<String> notifyParkingSpotInfo(ParkingSpotEntity parkingSpotEntity) {

        if (parkingSpotEntity == null) {
            return ResponseEntity.notFound().build();
        }

        ParkingSpotEntity newEntity = new ParkingSpotEntity();
        newEntity.setUuid(parkingSpotEntity.getUuid());
        newEntity.setBattery(parkingSpotEntity.getBattery());
        newEntity.setFreeSpot(parkingSpotEntity.isFreeSpot());

        if (parkingSpotRepository.findById(newEntity.getUuid()).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ParkingSpotEntity dbEntity = parkingSpotRepository.findById(newEntity.getUuid()).get();

        newEntity.setLng(dbEntity.getLng());
        newEntity.setLat(dbEntity.getLat());


        if (dbEntity.getStatus().equals(ParkingSpotStatus.UNREGISTERED)) {
            return ResponseEntity.badRequest().body("ESP not registered!");
        }

        long currentTime = System.currentTimeMillis();

        newEntity.setLastTimeReqSec(currentTime);
        newEntity.setStatus(ParkingSpotStatus.ONLINE);
        parkingSpotRepository.saveAndFlush(newEntity);
        return ResponseEntity.ok().build();

    }

    public ParkingSpotEntity isDeviceOffline(ParkingSpotEntity entity) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - entity.getLastTimeReqSec() >= FIFTEEN_MILI) {
            entity.setStatus(ParkingSpotStatus.OFFLINE);
        }

        if (entity.getStatus().equals(ParkingSpotStatus.ONLINE) && entity.getBattery() < 20) {
            entity.setStatus(ParkingSpotStatus.LOW_BATTERY);
        }
        parkingSpotRepository.saveAndFlush(entity);

        return entity;
    }
}
