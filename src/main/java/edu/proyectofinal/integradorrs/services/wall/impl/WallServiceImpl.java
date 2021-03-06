/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.proyectofinal.integradorrs.services.wall.impl;

import edu.proyectofinal.integradorrs.services.tweets.impl.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.proyectofinal.integradorrs.model.FollowersHistory;
import edu.proyectofinal.integradorrs.model.Token;
import edu.proyectofinal.integradorrs.model.Update;
import edu.proyectofinal.integradorrs.model.UnifiedUpdate;
import edu.proyectofinal.integradorrs.repositorys.FollowersHistoryRepository;
import edu.proyectofinal.integradorrs.services.facebook.FaceService;
import edu.proyectofinal.integradorrs.services.tweets.TweetsService;
import edu.proyectofinal.integradorrs.repositorys.UpdatesRepository;
import edu.proyectofinal.integradorrs.services.analytics.AnalyticsService;
import edu.proyectofinal.integradorrs.services.usuario.UsuarioService;
import edu.proyectofinal.integradorrs.services.wall.WallService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import twitter4j.Status;
import java.util.List;
import edu.proyectofinal.integradorrs.comparators.UpdatesSort;
import edu.proyectofinal.integradorrs.services.favorites.FavoriteService;
import sun.reflect.generics.tree.Tree;

/**
 *
 * @author Emi
 */
@Service
public class WallServiceImpl implements WallService {


    @Autowired
    private FaceService facebookservice;
    @Autowired
    private TweetsService twitterservice;
    @Autowired
    private AnalyticsService analitycsService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private UpdatesRepository SocialFocusPosts;
   // UpdatesSort comparator = new UpdatesSort();
    
    @Override
    public Collection<UnifiedUpdate> getUnifiedWall(String email) {
       List<Update> anUpdates = new ArrayList();
       List<UnifiedUpdate> anUnifiedUpdates = new ArrayList();
       Update anUpdate = new Update();
       UnifiedUpdate anUnifiedUpdate = new UnifiedUpdate();
        Comparator<Update> comparator = new Comparator<Update>() {
         @Override
         public int compare(Update left, Update right) {
             return left.getCreationDate().toInstant().compareTo(right.getCreationDate().toInstant());
         }
     };
       Collection<facebook4j.Post>  aFacebookPosts = facebookservice.getUserTimeline(email);
       Collection<twitter4j.Status> aTwitterPosts  = twitterservice.getUserTimeline(email);
       
       if(aFacebookPosts != null)
       {
            for(facebook4j.Post aFBPost: aFacebookPosts)
            {
                anUpdate.setEmail(email);
                anUpdate.setSocialnetwork("Facebook");
                anUpdate.setTexto(aFBPost.getMessage());
                anUpdate.setcreationdate(aFBPost.getCreatedTime());
                anUpdate.setid(aFBPost.getId());
                if(aFBPost.getComments() == null)
                {
                 anUpdate.setcomments(0);
                }else{
                     anUpdate.setcomments(aFBPost.getComments().size());
                }
                if(aFBPost.getLikes() == null)
                {              
                    anUpdate.setlikes(0);
                }else{
                    anUpdate.setlikes(aFBPost.getLikes().size());
                }
                if(aFBPost.getSharesCount() == null)
                {
                     anUpdate.setshares(0);
                }else
                {
                   anUpdate.setshares(aFBPost.getSharesCount());
                }
                anUpdates.add(anUpdate);
                anUpdate = new Update();
            }
       }
       if(aTwitterPosts != null)
       {
            for(twitter4j.Status aTWPost: aTwitterPosts)
            {
                anUpdate.setEmail(email);
                anUpdate.setSocialnetwork("Twitter");
                anUpdate.setTexto(aTWPost.getText());
                anUpdate.setcreationdate(aTWPost.getCreatedAt());
                anUpdate.setid(String.valueOf(aTWPost.getId()));
                anUpdate.setcomments(aTWPost.getUserMentionEntities().length);
                anUpdate.setlikes(aTWPost.getFavoriteCount());
                anUpdate.setshares(aTWPost.getRetweetCount());
                anUpdates.add(anUpdate);
                anUpdate = new Update();
            }
       }
       Collections.sort(anUpdates);
       
       Map<String, List<Update>> map = new java.util.HashMap<String, List<Update>>();
        for (Update item : anUpdates) {
         List<Update> list = map.get(item.getTexto());
          if (list == null) {
            list = new ArrayList<Update>();
            map.put(item.getTexto(), list);
          }
          list.add(item);
        }
        
        for(Map.Entry<String, List<Update>> entry : map.entrySet())
        {
            if(entry.getValue().size() >1)
            {
               //Posteo múltiple
               anUnifiedUpdate.setEmail(email);
               anUnifiedUpdate.setid_fb(entry.getValue().get(0).getid());
               anUnifiedUpdate.setid_tw(entry.getValue().get(1).getid());
               anUnifiedUpdate.setcreationdate(entry.getValue().get(0).getCreationDate());
               anUnifiedUpdate.setTexto(entry.getKey());
               anUnifiedUpdate.setid("0");
               anUnifiedUpdate.setSocialnetwork("T/F");
               anUnifiedUpdate.setLikes_fb(entry.getValue().get(0).getlikes());
               anUnifiedUpdate.setLikes_tw(entry.getValue().get(1).getlikes());
               anUnifiedUpdate.setComments_fb(entry.getValue().get(0).getcomments());
               anUnifiedUpdate.setComments_tw(entry.getValue().get(1).getcomments());
               anUnifiedUpdate.setShares_fb(entry.getValue().get(0).getshares());
               anUnifiedUpdate.setShares_tw(entry.getValue().get(1).getshares());
            }else{
                if(entry.getValue().get(0).getSocialnetwork() == "Facebook")
                {
                    anUnifiedUpdate.setEmail(email);
                    anUnifiedUpdate.setid_fb(entry.getValue().get(0).getid());
                    anUnifiedUpdate.setid_tw("-1");
                    anUnifiedUpdate.setcreationdate(entry.getValue().get(0).getCreationDate());
                    anUnifiedUpdate.setTexto(entry.getKey());
                    anUnifiedUpdate.setid("0");
                    anUnifiedUpdate.setSocialnetwork(entry.getValue().get(0).getSocialnetwork());
                    anUnifiedUpdate.setLikes_fb(entry.getValue().get(0).getlikes());
                    anUnifiedUpdate.setLikes_tw(-1);
                    anUnifiedUpdate.setComments_fb(entry.getValue().get(0).getcomments());
                    anUnifiedUpdate.setComments_tw(-1);
                    anUnifiedUpdate.setShares_fb(entry.getValue().get(0).getshares());
                    anUnifiedUpdate.setShares_tw(-1);
                }else{
                    anUnifiedUpdate.setEmail(email);
                    anUnifiedUpdate.setid_fb("-1");
                    anUnifiedUpdate.setid_tw(entry.getValue().get(0).getid());
                    anUnifiedUpdate.setcreationdate(entry.getValue().get(0).getCreationDate());
                    anUnifiedUpdate.setTexto(entry.getKey());
                    anUnifiedUpdate.setid("0");
                    anUnifiedUpdate.setSocialnetwork(entry.getValue().get(0).getSocialnetwork());
                    anUnifiedUpdate.setLikes_fb(-1);
                    anUnifiedUpdate.setLikes_tw(entry.getValue().get(0).getlikes());
                    anUnifiedUpdate.setComments_fb(-1);
                    anUnifiedUpdate.setComments_tw(entry.getValue().get(0).getcomments());
                    anUnifiedUpdate.setShares_fb(-1);
                    anUnifiedUpdate.setShares_tw(entry.getValue().get(0).getshares());
                }
            }
            if((anUnifiedUpdate.getid_fb() != "-1")&&(anUnifiedUpdate.getid_tw() != "-1"))
            {
                anUnifiedUpdate.setFavorite((favoriteService.IsFavorite(email, anUnifiedUpdate.getid_fb()) && (favoriteService.IsFavorite(email, anUnifiedUpdate.getid_tw()))) == true? 1:0);
                anUnifiedUpdate.setSocialfocus_post((IsOwnPost(email,"Facebook",anUnifiedUpdate.getid_fb())&& IsOwnPost(email,"Twitter",anUnifiedUpdate.getid_tw())) == true? 1:0);
            }else
            {
                if(anUnifiedUpdate.getid_fb() != "-1")
                {
                    anUnifiedUpdate.setFavorite(favoriteService.IsFavorite(email, anUnifiedUpdate.getid_fb()) == true ? 1:0);
                                    anUnifiedUpdate.setSocialfocus_post((IsOwnPost(email,"Facebook",anUnifiedUpdate.getid_fb())) == true? 1:0);
                }else
                {
                                        anUnifiedUpdate.setFavorite(favoriteService.IsFavorite(email, anUnifiedUpdate.getid_tw()) == true ? 1:0);
                                                        anUnifiedUpdate.setSocialfocus_post((IsOwnPost(email,"Twitter",anUnifiedUpdate.getid_tw())) == true? 1:0);
                }
            }
            anUnifiedUpdates.add(anUnifiedUpdate);
            anUnifiedUpdate = new UnifiedUpdate();
        }
       //anUnifiedUpdates = anUnifiedUpdates.subList(0, 29);
       Collections.sort(anUnifiedUpdates);
       return anUnifiedUpdates;
    }
    
    private Boolean IsOwnPost(String email, String SocialNetwork, String ID)
    {
        return SocialFocusPosts.findByEmailandSNandID(email, SocialNetwork, ID) == null;
    }
    
}
    
