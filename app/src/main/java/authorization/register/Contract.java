package authorization.register;

public interface Contract {

    interface RegisterView {

        void showProgressbar();

        void hideProgressbar();

        void onSuccess();

        void onFailed(String messsage);

    }

    interface RegisterListener {

        void onSuccess();

        void onFailed(String messsage);

    }

}
