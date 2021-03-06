package com.spantons.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.spantons.Interfaces.IUpdateable;
import com.spantons.stagesLevel.StagesLevel;

public class ParseXMLEntity {

	public static Entity getEntityFromXML(String _path, StagesLevel _stage, int _xMap, int _yMap){
		try {
			URL is = ParseXMLEntity.class.getResource(_path);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document document = builder.parse(is.toString());
			document.getDocumentElement().normalize();
		 
			if (document.hasChildNodes()) 
				return createEntity(document.getChildNodes(),_stage,_xMap,_yMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * @throws ClassNotFoundException 
	 * @throws DOMException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException **************************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Entity createEntity(NodeList childNodes, StagesLevel _stage, int _xMap, int _yMap) throws DOMException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		 
		if(childNodes != null && childNodes.getLength() > 0) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				 
				Node nNode = childNodes.item(i);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					Entity aux = new Entity(_stage, _xMap, _yMap);
						
					aux.description = eElement.getElementsByTagName("Description").item(0).getTextContent();
					aux.health = Double.parseDouble(eElement.getElementsByTagName("Health").item(0).getTextContent());
					aux.maxHealth = Double.parseDouble(eElement.getElementsByTagName("MaxHealth").item(0).getTextContent());
					aux.perversity = Integer.parseInt(eElement.getElementsByTagName("Perversity").item(0).getTextContent());
					aux.maxPerversity = Integer.parseInt(eElement.getElementsByTagName("MaxPerversity").item(0).getTextContent());
					aux.damage = Double.parseDouble(eElement.getElementsByTagName("Damage").item(0).getTextContent());
					aux.damageBackup = Double.parseDouble(eElement.getElementsByTagName("DamageBackup").item(0).getTextContent());
					aux.flinchingIncreaseDeltaTimePerversity = Integer.parseInt(eElement.getElementsByTagName("FlinchingIncreaseDeltaTimePerversity").item(0).getTextContent());
					aux.flinchingDecreaseDeltaTimePerversity = Integer.parseInt(eElement.getElementsByTagName("FlinchingDecreaseDeltaTimePerversity").item(0).getTextContent());
					aux.deltaForReduceFlinchingIncreaseDeltaTimePerversity = Integer.parseInt(eElement.getElementsByTagName("DeltaForReduceFlinchingIncreaseDeltaTimePerversity").item(0).getTextContent());
					aux.moveSpeed = Integer.parseInt(eElement.getElementsByTagName("MoveSpeed").item(0).getTextContent());
					aux.scale = Double.parseDouble(eElement.getElementsByTagName("Scale").item(0).getTextContent());
					aux.dead = Boolean.parseBoolean(eElement.getElementsByTagName("Dead").item(0).getTextContent());
					aux.facingRight = Boolean.parseBoolean(eElement.getElementsByTagName("FacingRight").item(0).getTextContent());
					aux.visible = Boolean.parseBoolean(eElement.getElementsByTagName("Visible").item(0).getTextContent());
					aux = EntityUtils.loadSprite(aux, eElement.getElementsByTagName("HUD").item(0).getTextContent(), eElement.getElementsByTagName("Sprite").item(0).getTextContent());
					aux.animation = new Animation();
					aux.currentAnimation = Entity.IDLE;
					aux.animation.setFrames(aux.sprites.get(Entity.IDLE));
					aux.animation.setDelayTime(1000);
					
					Class updateClass = Class.forName(
							"com.spantons.entity." + eElement.getElementsByTagName("Update").item(0).getTextContent());
					Class[] types = {Entity.class};
					Constructor constructor = updateClass.getConstructor(types);
					Object[] parameters = {aux};
					Object instance = constructor.newInstance(parameters);
					aux.update = (IUpdateable) instance;
					
					return aux;
				}
			}
		}
		return null;
	}
	
}
