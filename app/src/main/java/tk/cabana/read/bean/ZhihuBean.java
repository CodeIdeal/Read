package tk.cabana.read.bean;

import java.util.List;

/**
 * Created by k on 2016/2/19.
 */
public class ZhihuBean {

    /**
     * date : 20160220
     * stories : [{"images":["http://pic3.zhimg.com/6f57be04d6ea92c4d1004153d04562ba.jpg"],"type":0,"id":7898305,"ga_prefix":"022015","title":"为什么「相爱总是简单，相处太难」？"},{"images":["http://pic4.zhimg.com/f971a18c2b55eb4e8895b049502ad6db.jpg"],"type":0,"id":7737955,"ga_prefix":"022014","title":"五块钱丢了没感觉，买个甜筒掉地上真是分分钟哭出声"},{"images":["http://pic3.zhimg.com/fbf245508dd9f2d174f79d640f6b3642.jpg"],"type":0,"id":7894511,"ga_prefix":"022013","title":"车牌号成了新的信息泄露入口了吗？"},{"images":["http://pic1.zhimg.com/7fb92512c04fc02bcedf8427b762f3b0.jpg"],"type":0,"id":7741341,"ga_prefix":"022012","title":"大误 · 咏一位被声色犬马诱惑堕落的男青年"},{"images":["http://pic3.zhimg.com/6134e4d78ff2ab487189fc9e6d9d8d36.jpg"],"type":0,"id":7896218,"ga_prefix":"022011","title":"女性 30 岁后定期体检，就可能避免梅艳芳的悲剧重演"},{"images":["http://pic3.zhimg.com/1008173d923b46ce1038f782a321c4ae.jpg"],"type":0,"id":7895120,"ga_prefix":"022010","title":"重男轻女现象的存在，到底跟经济发达程度有没有关系？"},{"title":"喜欢做饭的体验，就是不小心爬到了食物链顶端","ga_prefix":"022009","images":["http://pic2.zhimg.com/d1898cb6dd2f34239096bd7d7e93197d.jpg"],"multipic":true,"type":0,"id":7896193},{"title":"这组极光太美，就算你去北极也不一定看得到（多图）","ga_prefix":"022008","images":["http://pic3.zhimg.com/9629f4a13e2dda6f56b4335469a174aa.jpg"],"multipic":true,"type":0,"id":7825419},{"images":["http://pic4.zhimg.com/65ced661ee46571ae53303460461e17f.jpg"],"type":0,"id":7895191,"ga_prefix":"022007","title":"欧洲难民危机愈演愈烈的时候，这个导演用镜头给出回答"},{"images":["http://pic3.zhimg.com/1c6f96d16b4a1898d2e095da7270f0e2.jpg"],"type":0,"id":7893514,"ga_prefix":"022007","title":"用手包住灯管，为什么手会呈红色？"},{"title":"周末干什么 · 这种神作，我等凡人也可以学一学","ga_prefix":"022007","images":["http://pic3.zhimg.com/981885d9148acef489e9f306edd2120e.jpg"],"multipic":true,"type":0,"id":7740943},{"images":["http://pic1.zhimg.com/64ab06bbe6c36400d0b2a719bc6bd890.jpg"],"type":0,"id":7896734,"ga_prefix":"022007","title":"读读日报 24 小时热门：即将上映的 30 部大片"},{"images":["http://pic2.zhimg.com/a975cfbbc3fba3df52a4d67c04914925.jpg"],"type":0,"id":7876405,"ga_prefix":"022006","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic2.zhimg.com/aa1b61c821909e1d9472fd972314685d.jpg","type":0,"id":7896734,"ga_prefix":"022007","title":"读读日报 24 小时热门：即将上映的 30 部大片"},{"image":"http://pic1.zhimg.com/fba2907a758cb35538e0acf969919fb4.jpg","type":0,"id":7896193,"ga_prefix":"022009","title":"喜欢做饭的体验，就是不小心爬到了食物链顶端"},{"image":"http://pic3.zhimg.com/2109d453c7ce04be4f2901007b41c14e.jpg","type":0,"id":7825419,"ga_prefix":"022008","title":"这组极光太美，就算你去北极也不一定看得到（多图）"},{"image":"http://pic3.zhimg.com/d03025c923c66a49dc2e4519b22f6842.jpg","type":0,"id":7893514,"ga_prefix":"022007","title":"用手包住灯管，为什么手会呈红色？"},{"image":"http://pic1.zhimg.com/4eedc8e64e2817c6386397014e325808.jpg","type":0,"id":7740943,"ga_prefix":"022007","title":"周末干什么 · 这种神作，我等凡人也可以学一学"}]
     */

    public String date;
    /**
     * images : ["http://pic3.zhimg.com/6f57be04d6ea92c4d1004153d04562ba.jpg"]
     * type : 0
     * id : 7898305
     * ga_prefix : 022015
     * title : 为什么「相爱总是简单，相处太难」？
     */

    public List<StoriesEntity> stories;
    /**
     * image : http://pic2.zhimg.com/aa1b61c821909e1d9472fd972314685d.jpg
     * type : 0
     * id : 7896734
     * ga_prefix : 022007
     * title : 读读日报 24 小时热门：即将上映的 30 部大片
     */

    public List<TopStoriesEntity> top_stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public void setTop_stories(List<TopStoriesEntity> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public List<TopStoriesEntity> getTop_stories() {
        return top_stories;
    }

    public static class StoriesEntity {
        public int type;
        public int id;
        public String ga_prefix;
        public String title;
        public List<String> images;

        public void setType(int type) {
            this.type = type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getImages() {
            return images;
        }
    }

    public static class TopStoriesEntity {
        public String image;
        public int type;
        public int id;
        public String ga_prefix;
        public String title;

        public void setImage(String image) {
            this.image = image;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public String getTitle() {
            return title;
        }
    }
}
