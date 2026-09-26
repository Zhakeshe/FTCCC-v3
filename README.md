# FTC Robot Code

## Drive

`robot.drive.drive(y,x,rx,speed)`  
Управляет движением робота.

- y — вперед/назад
- x — влево/вправо
- rx — поворот
- speed — скорость

```java
robot.drive.drive(1,0,0,1);
```

Ехать вперед.

`robot.drive.stop()`  
Остановить робота.

## Intake

`robot.intake.in()` — затягивать

`robot.intake.out()` — выкидывать

`robot.intake.stop()` — остановить

## Claw

`robot.claw.open()` — открыть

`robot.claw.close()` — закрыть

## Lift

`robot.lift.up()` — поднять

`robot.lift.down()` — опустить

`robot.lift.stop()` — остановить

## Autonomous

`goTo(x,y,heading)` — ехать к координате

```java
goTo(60,30,90);
```

`turn(degrees)` — повернуться

```java
turn(90);
```

## Pedro Pathing

`follower.update()` — обновить Pedro

`follower.isBusy()` — проверить, выполняется ли путь

`follower.getPose()` — получить позицию робота

`follower.getPose().getX()` — X

`follower.getPose().getY()` — Y

`follower.getPose().getHeading()` — угол