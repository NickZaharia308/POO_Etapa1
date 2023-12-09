package userEntities.specialEntities;

import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Host;
import userEntities.Users;
import userEntities.audio.Playlists;
import userEntities.audio.Songs;

import java.util.ArrayList;
import java.util.Comparator;

@Getter
public class PageMenu {
    Page currentPage = Page.HOMEPAGE;
    private final int maxShowed = 5;
    private String pageOwnerName;

    public enum Page {
        HOMEPAGE, LIKEDCONTENTPAGE, ARTISTPAGE, HOSTPAGE
    }


    public void setPage(Users user, Library library, String name) {
        Page usersCurrentPage = user.getPageMenu().currentPage;
        if (usersCurrentPage == Page.HOMEPAGE || user.getUsername().equals(name)) {
            createHomepage(user);
        } else if (usersCurrentPage == Page.LIKEDCONTENTPAGE) {
            createLikedContentPage(user);
        } else if (usersCurrentPage == Page.ARTISTPAGE) {
            setArtistPage(user, library, name);
        } else if (usersCurrentPage == Page.HOSTPAGE) {
            setHostPage(user, library, name);
        }
    }

    private void createHomepage(Users user) {
        ArrayList<Songs> likedSongs = sortLikedSongs(user.getLikedSongs());
        ArrayList<Playlists> followedPlaylists = user.getFollowedPlaylists();

        final int maxSongs = Math.min(likedSongs.size(), maxShowed);
        final int maxPlaylists = Math.min(followedPlaylists.size(), maxShowed);

        StringBuilder messageBuilder = new StringBuilder("Liked songs:\n\t[");
        for (int i = 0; i < maxSongs; i++) {
            messageBuilder.append(likedSongs.get(i).getName());

            // Add a comma if it's not the last element
            if (i < maxSongs - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]\n\nFollowed playlists:\n\t[");
        for (int i = 0; i < maxPlaylists; i++) {
            messageBuilder.append(followedPlaylists.get(i).getName());

            // Add a comma if it's not the last element
            if (i < maxPlaylists - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]");
        user.setCurrentPage(messageBuilder.toString());
    }

    private void createLikedContentPage(Users user) {
        ArrayList<Songs> likedSongs = user.getLikedSongs();
        ArrayList<Playlists> followedPlaylists = user.getFollowedPlaylists();

        StringBuilder messageBuilder = new StringBuilder("Liked songs:\n\t[");
        for (int i = 0; i < likedSongs.size(); i++) {
            messageBuilder.append(likedSongs.get(i).getName() + " - " + likedSongs.get(i).getArtist());

            // Add a comma if it's not the last element
            if (i < likedSongs.size() - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]\n\nFollowed playlists:\n\t[");
        for (int i = 0; i < followedPlaylists.size(); i++) {
            messageBuilder.append(followedPlaylists.get(i).getName() + " - " + followedPlaylists.get(i).getOwner());

            // Add a comma if it's not the last element
            if (i < followedPlaylists.size() - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]");
        user.setCurrentPage(messageBuilder.toString());
    }

    public void setArtistPage(Users user, Library library, String artistName) {
        Artist artist = null;
        for (Users searchedArtist : library.getUsers()) {
            // If the user has the artist's name and the user is an artist
            if (searchedArtist.getUsername().equals(artistName) && searchedArtist.getUserType() == Users.UserType.ARTIST) {
                artist = (Artist) searchedArtist;
                break;
            }
        }

        if (artist == null)
            return;

        user.setCurrentPage(artist.toString());
    }

    public void setHostPage(Users user, Library library, String hostName) {
        Host host = null;
        for (Users searchedHost : library.getUsers()) {
            // If the user has the host's name and the user is a host
            if (searchedHost.getUsername().equals(hostName) && searchedHost.getUserType() == Users.UserType.HOST) {
                host = (Host) searchedHost;
                break;
            }
        }

        if (host == null)
            return;

        user.setCurrentPage(host.toString());
    }

    private ArrayList<Songs> sortLikedSongs(final ArrayList<Songs> likedSongs) {
        ArrayList<Songs> sortedSongs = new ArrayList<>(likedSongs);
        sortedSongs.sort(Comparator.comparingLong(Songs::getNumberOfLikes).reversed());
        return sortedSongs;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageOwnerName(String pageOwnerName) {
        this.pageOwnerName = pageOwnerName;
    }
}
