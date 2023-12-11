package com.mojang.minecraft.model;

public final class ModelCache {
	private HumanoidModel humanoidModel = new HumanoidModel(0.0F);
	private HumanoidModel humanoidArmorModel = new HumanoidModel(1.0F);
	private CreeperModel creeperModel = new CreeperModel();
	private SkeletonModel skeletonModel = new SkeletonModel();
	private ZombieModel zombieModel = new ZombieModel();
	private QuadrupedModel quadrupedModel = new PigModel();
	private QuadrupedModel sheepModel = new SheepModel();
	private SpiderModel spiderModel = new SpiderModel();
	private SheepFurModel sheepFurModel = new SheepFurModel();

	public final BaseModel getModel(String name) {
		return (BaseModel)(name.equals("humanoid") ? this.humanoidModel : (name.equals("humanoid.armor") ? this.humanoidArmorModel : (name.equals("creeper") ? this.creeperModel : (name.equals("skeleton") ? this.skeletonModel : (name.equals("zombie") ? this.zombieModel : (name.equals("pig") ? this.quadrupedModel : (name.equals("sheep") ? this.sheepModel : (name.equals("spider") ? this.spiderModel : (name.equals("sheep.fur") ? this.sheepFurModel : null)))))))));
	}
}