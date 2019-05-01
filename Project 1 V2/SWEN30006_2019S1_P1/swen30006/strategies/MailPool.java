package strategies;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ListIterator;

import automail.MailItem;
import automail.PriorityMailItem;
import automail.Robot;
import exceptions.ItemTooHeavyException;

public class MailPool implements IMailPool {

	private class Item {
		int priority;
		int destination;
		MailItem mailItem;
		// Use stable sort to keep arrival time relative positions
		
		public Item(MailItem mailItem) {
			priority = (mailItem instanceof PriorityMailItem) ? ((PriorityMailItem) mailItem).getPriorityLevel() : 1;
			destination = mailItem.getDestFloor();
			this.mailItem = mailItem;
		}
	}
	
	public class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item i1, Item i2) {
			int order = 0;
			if (i1.priority < i2.priority) {
				order = 1;
			} else if (i1.priority > i2.priority) {
				order = -1;
			} else if (i1.destination < i2.destination) {
				order = 1;
			} else if (i1.destination > i2.destination) {
				order = -1;
			}
			return order;
		}
	}
	
	private LinkedList<Item> pool;
	private LinkedList<Robot> robots;
	
	private ArrayList<Item> pool2;
	private ArrayList<Robot> robots2;

	public MailPool(int nrobots){
		// Start empty
		pool2 = new ArrayList<Item>();
		robots2 = new ArrayList<Robot>();
	}

	public void addToPool(MailItem mailItem) {
		Item item = new Item(mailItem);
		pool2.add(item);
		pool2.sort(new ItemComparator());
	}
	
	@Override
	public void step() throws ItemTooHeavyException {
		try{
			ListIterator<Robot> i = robots2.listIterator();
			while (i.hasNext()) loadRobot(i);
		} catch (Exception e) { 
            throw e; 
        } 
	}
	
	ArrayList<Robot> robotsList = new ArrayList<>();
	
	private void loadRobot(ListIterator<Robot> i) throws ItemTooHeavyException {
		Robot robot = i.next();
		assert(robot.isEmpty());
	//	System.out.printf("P: %3d%n", pool2.size());
		ListIterator<Item> j = pool2.listIterator();
		
		if (pool2.size() > 0) {
			MailItem mail = j.next().mailItem;
			
			try {
				if (mail.getWeight() > Robot.INDIVIDUAL_MAX_WEIGHT && robot.isEmpty()) {
					robot.addToHand(mail); // hand first as we want higher priority delivered first	
					robotsList.add(robot);
					
					if (robotsList.size() > 1) {
						j.remove();
						
						for (Robot var : robotsList) {
							var.dispatch();

						}
						robotsList.clear();
					}
					
					i.remove(); // remove from mailPool queue
					
				}
				
				else if (mail.getWeight() <= Robot.INDIVIDUAL_MAX_WEIGHT && robot.isEmpty()) {
					robot.addToHand(mail); // hand first as we want higher priority delivered first	
					j.remove();
					
					if (pool2.size() > 0) {
						robot.addToTube(j.next().mailItem);
						j.remove();
					}
					
					robot.dispatch(); // send the robot off if it has any items to deliver
					i.remove();       // remove from mailPool queue
					
				}
				
				else {
					robot.dispatch(); // send the robot off if it has any items to deliver
					i.remove();       // remove from mailPool queue
				}
				

				


			} 
				
			catch (Exception e) { 
	            throw e; 
	        } 
			
		}
		
		
//		if (pool.size() > 0 && j.next().mailItem.getWeight() <= Robot.INDIVIDUAL_MAX_WEIGHT) {
//			try {
//			robot.addToHand(j.next().mailItem); // hand first as we want higher priority delivered first
//			j.remove();
//			if (pool.size() > 0) {
//				robot.addToTube(j.next().mailItem);
//				j.remove();
//			}
//			robot.dispatch(); // send the robot off if it has any items to deliver
//			i.remove();       // remove from mailPool queue
//			} catch (Exception e) { 
//	            throw e; 
//	        } 
//		}
		
//		else if (pool.size() > 0 && j.next().mailItem.getWeight() > Robot.INDIVIDUAL_MAX_WEIGHT) {
//			robot.addToHand(j.next().mailItem);
//			j.remove();
//			
//			if (robot.getTeamNumber() >= 2) {
//				robot.dispatch(); // send the robot off if it has any items to deliver
//				i.remove();       // remove from mailPool queue
//				robot.resetTeamNumber();
//				robot.setTeamMember(false);
//			}
//		}
	}

	@Override
	public void registerWaiting(Robot robot) { // assumes won't be there already
		robots2.add(robot);
	}

}
