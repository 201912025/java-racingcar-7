package racingcar.service;

import racingcar.domain.RacingCar;
import racingcar.validator.CarNameValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RacingCarService {
    private final List<RacingCar> racingCars;
    private final CarNameValidator carNameValidator;

    private static final String SPLIT_SEPARATOR =",";

    public RacingCarService(CarNameValidator carNameValidator) {
        this.carNameValidator = carNameValidator;
        this.racingCars = new ArrayList<>();
    }

    public void createRacingCars(String inputCarsName) {
        String[] carsNames = splitCarsName(inputCarsName);
        carNameValidator.validateCarNameIsEmpty(inputCarsName);
        createRacingCarsBySplitCarsName(carsNames);
    }

    public void advanceRacingCar() {
        for (RacingCar racingCarTemp : racingCars) {
            if (racingCarTemp.shouldAdvance()) {
                racingCarTemp.addAdvanceResult();
            }
        }
    }

    public List<String[]> extractCarNameAndAdvanceResult() {
        List<String[]> carNameAndAdvanceResultList = new ArrayList<>();
        for (RacingCar racingCarTemp : this.racingCars) {
            String carNameTemp = racingCarTemp.getCarName();
            String advanceResultTemp = charListToString(racingCarTemp.getAdvanceResults());
            carNameAndAdvanceResultList.add(new String[]{carNameTemp, advanceResultTemp});
        }
        return carNameAndAdvanceResultList;
    }

    public String charListToString(List<Character> advanceResults) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Character advanceSymbol : advanceResults) {
            stringBuilder.append(advanceSymbol);
        }
        return stringBuilder.toString();
    }

    public String[] splitCarsName(String inputCarsName) {
        return Arrays.stream(inputCarsName.split(SPLIT_SEPARATOR))
                .map(String::trim)
                .toArray(String[]::new);
    }

    public void createRacingCarsBySplitCarsName(String[] carsNames) {
        for (String carName : carsNames) {
            carNameValidator.validateCarNameLength(carName);
            this.racingCars.add(new RacingCar(carName, new ArrayList<>()));
        }
    }

    public String selectCarRacingWinners() {
        int maxAdvanceCount = findMaxAdvanceCount();
        List<String> winners = findWinners(maxAdvanceCount);
        return formatWinners(winners);
    }

    private int findMaxAdvanceCount() {
        return racingCars.stream()
                .mapToInt(car -> car.getAdvanceResults().size())
                .max()
                .orElse(0);
    }

    private List<String> findWinners(int maxAdvanceCount) {
        return racingCars.stream()
                .filter(car -> car.getAdvanceResults().size() == maxAdvanceCount)
                .map(RacingCar::getCarName)
                .collect(Collectors.toList());
    }

    private String formatWinners(List<String> winners) {
        return String.join(", ", winners);
    }
}
