package com.observer;

import java.util.Vector;

/**
 * @author nidayu
 * @Description: 观察者模式
 * @date 2015/9/19
 */
abstract class Subject {
    private Vector<Observer> obs = new Vector<Observer>();

    public void addObserver(Observer obs){
        this.obs.add(obs);
    }
    public void delObserver(Observer obs){
        this.obs.remove(obs);
    }
    protected void notifyObserver(){
        for(Observer o: obs){
            o.update();
        }
    }
    public abstract void doSomething();
}

//被观察者
class ConcreteSubject extends Subject {
    public void doSomething(){
        System.out.println("被观察者事件发生");
        this.notifyObserver();
    }
}

//观察者接口
interface Observer {
    public void update();
}

//观察者1
class ConcreteObserver1 implements Observer {
    public void update() {
        System.out.println("观察者1收到信息，并进行处理。");
    }
}

//观察者2
class ConcreteObserver2 implements Observer {
    public void update() {
        System.out.println("观察者2收到信息，并进行处理。");
    }
}

public class Client {
    public static void main(String[] args){
        Subject sub = new ConcreteSubject();
        Observer o1 = new ConcreteObserver1();
        Observer o2 = new ConcreteObserver2();
        sub.addObserver(o1); //添加观察者1
        sub.addObserver(o2); //添加观察者2
//        sub.delObserver(o2);
        sub.doSomething();
    }
}
