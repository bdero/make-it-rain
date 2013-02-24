package com.awesome.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.awesome.map.Map;

public class Enemy extends Entity 
{
	protected enum eAIState
	{
		kAIState_Idle,
		kAIState_PursueTarget,
		kAIState_AttackTarget,
		kAIState_Flee,
	}
	
	protected Entity mTargetEntity = null;
	protected eAIState mCurrentState = eAIState.kAIState_Idle;
	protected long mNextThinkTime = 0;
	protected static final long mThinkDelay = 200;
	protected float mRange = Map.TILE_SIZE/2.0f;
	
	public Enemy()
	{
		super();
		
		mScale = 5.0f;
		mWalkFriction = 0.98f;
		mMoveVelocity = 5.0f;
	}
	
	protected Entity FindTarget()
	{
		EntityManager entityManager = EntityManager.getInstance();
		for (Entity entity : entityManager.getEntities())
		{
			if (entity instanceof Player)
			{
				return entity;
			}
		}
		return null;
	}
	
	protected void Think()
	{
		switch(mCurrentState)
		{
			case kAIState_Idle:
			{
				//If we have a target, pursue it
				if (mTargetEntity != null)
				{
					mCurrentState = eAIState.kAIState_PursueTarget;
					break;
				}
				
				//Try to find a target
				Entity target = FindTarget();
				if (target != null)
				{
					//We found a target, so pursue it
					mTargetEntity = target;
					mCurrentState = eAIState.kAIState_PursueTarget;
					break;
				}
				
				break;
			}
			case kAIState_PursueTarget:
			{
				//If we don't have a target, go idle
				if (mTargetEntity == null)
				{
					mCurrentState = eAIState.kAIState_Idle;
					break;
				}
				
				//Calculate the direction to the target 
				float targetDir = mTargetEntity.getPosition().sub(getPosition()).x;
				float targetDist = mTargetEntity.getPosition().distance(getPosition());
				
				//We're in range, attack!
				if (targetDist >= -mRange && targetDist <= mRange)
				{
					mCurrentState = eAIState.kAIState_AttackTarget;
					break;
				}
				else
				{
					eControlFlag movementFlag = eControlFlag.kControlFlag_Right;
					if (targetDir < 0.0f)
					{
						movementFlag = eControlFlag.kControlFlag_Left;
					}
					SetControlFlag(movementFlag, true);
					break;
				}
			}
			case kAIState_AttackTarget:
			{
				//If we don't have a target, go idle
				if (mTargetEntity == null)
				{
					mCurrentState = eAIState.kAIState_Idle;
					break;
				}
				
				//Calculate the direction to the target 
				float targetDir = mTargetEntity.getPosition().sub(getPosition()).x;
				float targetDist = mTargetEntity.getPosition().distance(getPosition());
				
				//Pursue the target since we're out of range
				if (targetDist < -mRange || targetDist > mRange)
				{
					mCurrentState = eAIState.kAIState_PursueTarget;
					break;
				}
				
				SetControlFlag(eControlFlag.kControlFlag_Jump, true);
				
				break;
			}
			case kAIState_Flee:
			{
				break;
			}
			default:
			{
				break;
			}
		}
	}

	public void init(GameContainer gc) throws SlickException 
	{
		image = new Image("res/images/enemy.png");
		boundingBox.setBounds(boundingBox.getX(),
				boundingBox.getY(),
				Map.TILE_SIZE,
				Map.TILE_SIZE);
	}

	public void update(GameContainer gc, int delta) throws SlickException
	{
		long currentTime = gc.getTime();
		if (currentTime >= mNextThinkTime)
		{
			mNextThinkTime = currentTime + mThinkDelay;
			Think();
		}
		super.update(gc, delta);
	}
}
