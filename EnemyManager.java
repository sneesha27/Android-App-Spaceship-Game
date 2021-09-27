package sgeorgiev.org.spaceshipgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class EnemyManager {
    //array list to store enemies
    private ArrayList<Enemy> enemies;
    private boolean explode = false;
    private Explosion explosion;
    private ArrayList<Enemy> destroyed;
    private int count;
    private Sound sound;
    private ArrayList<Gem> gems;

    public EnemyManager(int enemyCount) {
        this.count = enemyCount;
        enemies = new ArrayList<>();
        destroyed = new ArrayList<>();
        gems = new ArrayList<>();
        populateEnemies(count);
        sound = new Sound();
    }

    public void populateEnemies(int count) {
        for(int i = 0; i < count; i++) {
            enemies.add(new Enemy());
        }
    }

    public void update(Player player) {

        int diff = 0;
        if(enemies.size() < count) {
            diff = count - enemies.size();
        }

        for(int i = 0; i < diff; i++)
            enemies.add(new Enemy());


        for (Enemy e : enemies) {
            //update enemies
            e.update(player.getSpeed());
            //check for collisions
            for(Projectile p : player.getProjectiles()) {
                if (Rect.intersects(p.getHitBox(), e.getHitBox())) {
                    //set explosion to true so we know we have to draw it
                    explode = true;
                    //and initialise the object
                    explosion = new Explosion(e.getX(), e.getY());
                    //add the ship to be destryed to the array list
                    destroyed.add(e);
                    player.destroyProjectile(p);
                    //increment score
                    Constants.SCORE += 10;
                    gems.add(new Gem(e.getX(), e.getY()));
                    sound.playExplosion();
                }
            }

            for(Projectile p : e.getProjectile()) {
                if (player.isShieldActive() && Rect.intersects(player.getShield().getHitBox(), p.getHitBox())) {
                    player.setShield(false);
                    e.removeProj(p);
                } else if ((Rect.intersects(player.getHitBoxWings(), p.getHitBox()) ||
                        Rect.intersects(player.getHitBox(), p.getHitBox())) && !player.isShieldActive()) {
                    Constants.GAMEOVER_TIME = System.currentTimeMillis();
                    Constants.GAME_OVER = true;
                }
            }

            if(player.isShieldActive() && Rect.intersects(player.getShield().getHitBox(), e.getHitBox())) {
                player.setShield(false);
                destroyed.add(e);
            } else if( (Rect.intersects(player.getHitBox(), e.getHitBox()) || Rect.intersects(player.getHitBoxWings(), e.getHitBox()) )
                    && !player.isShieldActive()) {
                    Constants.GAME_OVER = true;
                    Constants.GAMEOVER_TIME = System.currentTimeMillis();
            }
        }
        //destroy all ships AFTER the loop
        enemies.removeAll(destroyed);

        ArrayList<Gem> removedGems = new ArrayList<>();
        for(Gem g : gems) {
            if(player.gemCollision(g.getHitBox())) {
                Constants.SCORE += g.getPoints();
                if(g.isShield())
                    player.setShield(true);
                removedGems.add(g);
            }
            g.update(player.getSpeed());
        }
        gems.removeAll(removedGems);

        //every 9 seconds (60 fps * 9 seconds) add a new enemy to increase the difficulty of the game
        //the calculation is not particularly accurate but it works
        if(Constants.FRAME_COUNT % (60 * 9) == 0 && enemies.size() < 6 && Constants.FRAME_COUNT != 0) {
            enemies.add(new Enemy());
            count++;
        }
    }

    //draw
    public void draw(Canvas canvas, Paint paint) {
        for (Enemy e : enemies) {
            //draw each space ship
            e.draw(canvas, paint);
        }

        for(Gem g : gems)
            g.draw(canvas, paint);
        //if we need an explosion
        if(explode) {
            //draw it
            explosion.draw(canvas, paint);
            explode = false;
        }
    }
}
