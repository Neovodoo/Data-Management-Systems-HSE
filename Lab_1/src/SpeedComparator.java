import java.util.Comparator;
public class SpeedComparator implements Comparator<Racer>{
    /**
     * Compares two Racers based on maxSpeed
     *
     * @return
     * 		returns 1 if the first Racer has a higher speed
     * 		0 if both Racers have the same speed
     * 		-1 if the second Racer has the higher speed
     */
    public int compare(Racer o1,Racer o2) {
        Racer left = o1;
        Racer right = o2;
        if(left.getMaxSpeed() < right.getMaxSpeed())
            return -1;
        else if(left.getMaxSpeed() > right.getMaxSpeed())
            return 1;
        else
            return 0;
    }

}